package net.capozi.menagerie.common.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ChainedEffect extends StatusEffect {
    public ChainedEffect() {
        super(StatusEffectCategory.HARMFUL, 0x5A5A5A); // grey-ish color
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if ((entity instanceof PlayerEntity player)) {
            if (amplifier >= 0) {
                // Cancel all movement
                player.setVelocity(Vec3d.ZERO);
                player.velocityModified = true;
                // (Optional) cancel sneaking/jumping if client-side
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
}
