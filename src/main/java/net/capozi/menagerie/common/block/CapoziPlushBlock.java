package net.capozi.menagerie.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class CapoziPlushBlock extends Block {
    public CapoziPlushBlock(Settings settings) {
        super(settings);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
