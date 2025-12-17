package net.capozi.menagerie.server.cca;

import net.capozi.menagerie.common.entity.HealthUtils;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class BoundAccursedComponentImpl implements BoundAccursedComponent {
    private boolean hasAccursed = false;
    @Override
    public boolean hasAccursed() {
        return hasAccursed;
    }
    @Override
    public void setHasAccursed(boolean value) {
        this.hasAccursed = value;
    }
    @Override
    public void tickAccursed(PlayerEntity player) {
        if (hasAccursed()) {
            var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (attribute != null && attribute.getModifier(HealthUtils.EXTRA_HEARTS_UUID) == null) {
                HealthUtils.addExtraHearts(player, 10.0);
            }
            player.setAir(300);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 70, 1, false, false, false));
        }
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        this.hasAccursed = tag.getBoolean("HasAccursed");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("HasAccursed", hasAccursed);
    }
}
