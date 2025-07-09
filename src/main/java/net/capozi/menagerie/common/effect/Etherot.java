package net.capozi.menagerie.common.effect;

import net.capozi.menagerie.common.entity.HealthUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class Etherot extends StatusEffect {
    public Etherot() {
        super(StatusEffectCategory.HARMFUL, 0x19fff7);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if ((entity instanceof PlayerEntity player)) {
            if (amplifier == 0) {
                HealthUtils.reduceMaxHealth(player, 2);
            } else if (amplifier == 1) {
                HealthUtils.reduceMaxHealth(player, 4);
            } else if (amplifier == 2) {
                HealthUtils.reduceMaxHealth(player, 6);
            } else if (amplifier >= 3) {
                HealthUtils.reduceMaxHealth(player, 8);
            }
        }
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Run applyUpdateEffect every tickEtherot
    }
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player) {
            HealthUtils.removeExtraHearts(player);
        }
    }
}
