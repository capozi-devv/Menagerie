package net.capozi.menagerie.common.effect;

import net.capozi.menagerie.common.entity.HealthUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class RuinousEffect extends StatusEffect {
    public RuinousEffect() {
        super(StatusEffectCategory.HARMFUL, 0x6e0c19);
    }
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        HealthUtils.reduceMaxHealth((PlayerEntity)entity, (amplifier + 1) * 4);
        super.onApplied(entity, attributes, amplifier);
    }
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        HealthUtils.removeExtraHearts((PlayerEntity)entity);
    }
}
