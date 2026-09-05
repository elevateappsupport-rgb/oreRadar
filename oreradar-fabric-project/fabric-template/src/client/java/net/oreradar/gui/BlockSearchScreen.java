package net.oreradar.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.oreradar.OreRadarClient;
import net.oreradar.TrackedBlocks;

import java.util.ArrayList;
import java.util.List;

public class BlockSearchScreen extends Screen {

    private TextFieldWidget searchField;
    private final List<Block> filtered = new ArrayList<>();

    private static final int SLOT_SIZE = 24;
    private static final int COLUMNS = 8;
    private static final int GRID_TOP = 40;

    public BlockSearchScreen() {
        super(Text.literal("OreRadar - Block auswählen"));
    }

    @Override
    protected void init() {
        searchField = new TextFieldWidget(
                this.textRenderer,
                this.width / 2 - 100, 15, 200, 20,
                Text.literal("Suche")
        );
        searchField.setChangedListener(this::updateFilter);
        addSelectableChild(searchField);
        setInitialFocus(searchField);
        updateFilter("");
    }

    private void updateFilter(String query) {
        filtered.clear();
        String q = query.toLowerCase();
        for (Block block : TrackedBlocks.ALL) {
            String name = Text.translatable(block.getTranslationKey()).getString().toLowerCase();
            if (name.contains(q)) {
                filtered.add(block);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        searchField.render(context, mouseX, mouseY, delta);

        int startX = this.width / 2 - (COLUMNS * SLOT_SIZE) / 2;
        int hoveredIndex = -1;

        for (int i = 0; i < filtered.size(); i++) {
            int col = i % COLUMNS;
            int row = i / COLUMNS;
            int x = startX + col * SLOT_SIZE;
            int y = GRID_TOP + row * SLOT_SIZE;

            boolean hovered = mouseX >= x && mouseX < x + SLOT_SIZE && mouseY >= y && mouseY < y + SLOT_SIZE;
            if (hovered) {
                context.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, 0x80FFFFFF);
                hoveredIndex = i;
            }

            ItemStack stack = new ItemStack(filtered.get(i).asItem());
            context.drawItem(stack, x + 4, y + 4);
        }

        if (hoveredIndex >= 0) {
            context.drawTooltip(
                    this.textRenderer,
                    Text.translatable(filtered.get(hoveredIndex).getTranslationKey()),
                    mouseX, mouseY
            );
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = this.width / 2 - (COLUMNS * SLOT_SIZE) / 2;

        for (int i = 0; i < filtered.size(); i++) {
            int col = i % COLUMNS;
            int row = i / COLUMNS;
            int x = startX + col * SLOT_SIZE;
            int y = GRID_TOP + row * SLOT_SIZE;

            if (mouseX >= x && mouseX < x + SLOT_SIZE && mouseY >= y && mouseY < y + SLOT_SIZE) {
                OreRadarClient.TRACKER.setSelectedBlock(filtered.get(i));
                this.close();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPauseGame() {
        return false; // Singleplayer läuft im Hintergrund weiter, wie bei der Inventar-GUI
    }
}
