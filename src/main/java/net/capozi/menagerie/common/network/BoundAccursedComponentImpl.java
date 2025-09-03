package net.capozi.menagerie.common.network;

import net.capozi.menagerie.common.entity.HealthUtils;
import net.capozi.menagerie.foundation.EffectInit;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class BoundAccursedComponentImpl implements BoundAccursedComponent{
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
            HealthUtils.addExtraHearts(player, 10.0);
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
