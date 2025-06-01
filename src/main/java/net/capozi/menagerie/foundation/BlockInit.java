package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockInit {
    /** public static final Block VITRIFIED_SOUL_SAND = registerBlock("vitrified_soul_sand",
            new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)));
    public static final Block ETHER_CAULDRON = registerBlock("ether_cauldron",
            new EtherCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON))); **/
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
}
