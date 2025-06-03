package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.block.CapoziPlushBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockInit {
    private static Block registerBlock(String name, Block block) {
        registerBlockItems(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Menagerie.MOD_ID, name), block);
    }
    private static Item registerBlockItems(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Menagerie.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    public static void registerBlocks() {

    }
    public static final Block CAPOZI_PLUSH = registerBlock("capozi_plush", new CapoziPlushBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).nonOpaque()));
}
