package net.oreradar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.oreradar.gui.BlockSearchScreen;
import org.lwjgl.glfw.GLFW;

public class OreRadarClient implements ClientModInitializer {

    public static final String MOD_ID = "oreradar";

    public static KeyBinding openMenuKey;
    public static final TargetTracker TRACKER = new TargetTracker();
    public static OreRadarConfig CONFIG;

    @Override
    public void onInitializeClient() {
        CONFIG = OreRadarConfig.load();

        // Diese Taste erscheint automatisch unter Optionen -> Steuerung -> "OreRadar"
        // und kann dort vom Spieler jederzeit umbelegt werden.
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.oreradar.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.oreradar.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new BlockSearchScreen());
                }
            }
            TRACKER.tick(client);
        });

        // Zeichnet die lila Linie zum Zielblock
        WorldRenderEvents.LAST.register(TRACKER::render);

        // Zeichnet das Label oben rechts
        HudRenderCallback.EVENT.register(this::renderHud);
    }

    private void renderHud(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;

        int screenWidth = context.getScaledWindowWidth();
        String keyName = openMenuKey.getBoundKeyLocalizedText().getString();
        String blockName = TRACKER.getSelectedBlockName();

        Text line1 = Text.literal("OreRadar").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD);
        Text line2 = Text.literal("Ziel: " + blockName).formatted(Formatting.WHITE);
        Text line3 = Text.literal("Taste: " + keyName).formatted(Formatting.GRAY);

        int y = 6;
        drawRightAligned(context, line1, screenWidth, y); y += 10;
        drawRightAligned(context, line2, screenWidth, y); y += 10;
        drawRightAligned(context, line3, screenWidth, y);
    }

    private void drawRightAligned(DrawContext context, Text text, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.textRenderer.getWidth(text);
        context.drawTextWithShadow(client.textRenderer, text, screenWidth - width - 6, y, 0xFFFFFF);
    }
}
