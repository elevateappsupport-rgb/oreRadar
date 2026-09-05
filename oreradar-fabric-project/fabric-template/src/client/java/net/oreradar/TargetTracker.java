package net.oreradar;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class TargetTracker {

    private Block selectedBlock;
    private BlockPos nearestPos;
    private int tickCounter = 0;

    public void setSelectedBlock(Block block) {
        this.selectedBlock = block;
        this.nearestPos = null;
        this.tickCounter = 0; // sofort neu suchen, nicht erst nach Intervall warten
    }

    public String getSelectedBlockName() {
        if (selectedBlock == null) return "-";
        return Text.translatable(selectedBlock.getTranslationKey()).getString();
    }

    public void tick(MinecraftClient client) {
        if (selectedBlock == null || client.world == null || client.player == null) return;

        int interval = Math.max(1, OreRadarClient.CONFIG.searchIntervalTicks);
        if (tickCounter % interval == 0) {
            search(client.world, client.player);
        }
        tickCounter++;
    }

    /**
     * WICHTIG (Einschränkung): Ein Client kann nur Blöcke in geladenen Chunks
     * sehen - also nur die Chunks, die gerade um dich herum simuliert/gerendert
     * werden (abhängig von deiner "Render Distance"). Ein Block, der 1000
     * Blöcke entfernt liegt, wird nur gefunden, wenn dieser Bereich auch
     * geladen ist (z.B. durch vorheriges Erkunden oder eine hohe Render
     * Distance). Für eine echte "über die ganze Welt"-Suche müsste man
     * direkt die Welt-Dateien (.mca) auf der Festplatte einlesen - das ist
     * ein guter nächster Lernschritt, aber deutlich komplexer.
     */
    private void search(World world, PlayerEntity player) {
        int radius = Math.max(1, OreRadarClient.CONFIG.searchRadius);
        BlockPos center = player.getBlockPos();

        BlockPos best = null;
        double bestDistSq = Double.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            int chunkX = (center.getX() + dx) >> 4;
            for (int dz = -radius; dz <= radius; dz++) {
                int chunkZ = (center.getZ() + dz) >> 4;
                if (!world.isChunkLoaded(chunkX, chunkZ)) continue;

                for (int dy = -radius; dy <= radius; dy++) {
                    BlockPos pos = center.add(dx, dy, dz);
                    if (world.getBlockState(pos).getBlock() == selectedBlock) {
                        double distSq = pos.getSquaredDistance(center);
                        if (distSq < bestDistSq) {
                            bestDistSq = distSq;
                            best = pos.toImmutable();
                        }
                    }
                }
            }
        }

        this.nearestPos = best;
    }

    public void render(WorldRenderContext context) {
        if (selectedBlock == null || nearestPos == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Vec3d camPos = context.camera().getPos();
        Vec3d start = client.player.getEyePos().subtract(camPos);
        Vec3d end = Vec3d.ofCenter(nearestPos).subtract(camPos);

        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer buffer = immediate.getBuffer(RenderLayer.getLines());

        matrices.push();
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float r = 0.65f, g = 0.2f, b = 1.0f, a = 1.0f;

        buffer.vertex(matrix, (float) start.x, (float) start.y, (float) start.z)
                .color(r, g, b, a)
                .normal(0, 1, 0);
        buffer.vertex(matrix, (float) end.x, (float) end.y, (float) end.z)
                .color(r, g, b, a)
                .normal(0, 1, 0);

        immediate.draw();
        matrices.pop();
    }
}
