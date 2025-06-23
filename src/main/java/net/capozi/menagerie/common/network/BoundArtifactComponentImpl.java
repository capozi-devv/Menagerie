package net.capozi.menagerie.common.network;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.entity.HealthUtils;
import net.capozi.menagerie.foundation.EffectInit;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoundArtifactComponentImpl implements BoundArtifactComponent, AutoSyncedComponent {
    private boolean hasArtifact = false;
    public BoundArtifactComponentImpl() {}
    @Override
    public boolean hasArtifact() {
        return hasArtifact;
    }
    @Override
    public void setHasArtifact(boolean value) {
        this.hasArtifact = value;
    }

    @Override
    public void tickArtifact(PlayerEntity player) {
        if (hasArtifact() && !player.hasStatusEffect(EffectInit.ETHEROT)) {
            player.setFireTicks(0);
            player.extinguish();
        }
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        this.hasArtifact = tag.getBoolean("HasArtifact");
    }
    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("HasArtifact", hasArtifact);
    }
}