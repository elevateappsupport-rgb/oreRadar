package net.oreradar;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.List;

/**
 * Alle Blöcke, die im Suchmenü auftauchen.
 * Einfach hier weitere Blocks.XXX Einträge ergänzen, wenn du mehr willst.
 */
public class TrackedBlocks {
    public static final List<Block> ALL = List.of(
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.GRAVEL,
            Blocks.NETHERITE_BLOCK,
            Blocks.DIAMOND_BLOCK, Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK,
            Blocks.EMERALD_BLOCK, Blocks.LAPIS_BLOCK, Blocks.REDSTONE_BLOCK,
            Blocks.COAL_BLOCK, Blocks.COPPER_BLOCK,
            Blocks.AMETHYST_CLUSTER,
            Blocks.SPAWNER, Blocks.TRIAL_SPAWNER, Blocks.VAULT,
            Blocks.CHEST
    );
}
