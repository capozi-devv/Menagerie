package net.capozi.menagerie.common.effect;

import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ChainedEffect extends StatusEffect {
    public ChainedEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x5A5A5A);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if ((entity instanceof PlayerEntity player)) {
            if (hasCameraItem((ServerPlayerEntity)player)) {
                player.removeStatusEffect(this);
                return;
            }
            if (amplifier >= 0) {
                player.setVelocity(Vec3d.ZERO);
                player.velocityModified = true;
                if (!player.getWorld().isClient) {
                    player.setSneaking(false);
                }
            }
        }
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    private boolean hasCameraItem(ServerPlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(ItemInit.CAMERA_OF_THE_OTHERSIDE)) {
                return false;
            }
        }
        return true;
    }
}
