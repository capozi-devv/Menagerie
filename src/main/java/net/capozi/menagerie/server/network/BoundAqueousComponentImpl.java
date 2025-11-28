package net.capozi.menagerie.server.network;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class BoundAqueousComponentImpl implements BoundAqueousComponent{
    private boolean aqueous = false;
    public BoundAqueousComponentImpl() {}
    @Override
    public boolean hasAqueous() {
        return aqueous;
    }
    @Override
    public void setHasAqueous(boolean value) {
        this.aqueous = value;
    }
    @Override
    public void tickAqueous(PlayerEntity player) {
        if (hasAqueous()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 220, 0, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 220, 0, true, false, false));
            if (player.isSubmergedInWater()) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 220, 1, false, false, false));
            }
        }
    }
    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }
    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }
}
