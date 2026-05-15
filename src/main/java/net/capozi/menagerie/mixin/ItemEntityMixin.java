package net.capozi.menagerie.mixin;

import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {
    @Shadow
    public abstract ItemStack getStack();
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Override
    public boolean collidesWith(Entity other) {
        if (other instanceof FallingBlockEntity block) {
            if (block.getBlockState() == Blocks.ANVIL.getDefaultState()) {
                if (getStack().getItem() == Items.NETHERITE_INGOT) {
                    this.dropStack(new ItemStack(ItemInit.KINEMATIC_CORE, getStack().getCount()));
                    this.discard();
                    this.playSound(SoundEvents.BLOCK_ANVIL_USE, 1, 1);
                }
            }
        }
        return super.collidesWith(other);
    }
    @Override
    protected void onBlockCollision(BlockState state) {
        if (state.isOf(Blocks.FIRE) && this.getStack().getItem().equals(Items.NETHER_STAR)) {
            if (getWorld() instanceof ServerWorld serverWorld) {
                this.dropStack(new ItemStack(ItemInit.FLYWHEEL_FRAMEWORK, this.getStack().getCount()));
                this.discard();
                this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 1, 1);
            }
        }
    }
}
