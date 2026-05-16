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
            if (block.getBlockState().getBlock() == Blocks.ANVIL) {
                if (getStack().getItem() == Items.NETHERITE_INGOT) {
                    this.dropStack(new ItemStack(ItemInit.KINEMATIC_CORE, getStack().getCount()));
                    this.playSound(SoundEvents.BLOCK_ANVIL_USE, 1, 1);
                    this.discard();
                    return super.collidesWith(other);
                }
                if (getStack().getItem() == Items.NETHER_STAR) {
                    this.dropStack(new ItemStack(ItemInit.FLYWHEEL_FRAMEWORK, this.getStack().getCount()));
                    this.playSound(SoundEvents.BLOCK_ANVIL_USE, 1, 1);
                    this.discard();
                    return super.collidesWith(other);
                }
            }
        }
        return super.collidesWith(other);
    }
}