package net.capozi.menagerie.server.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.capozi.menagerie.Menagerie;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class DecryptorsEyeSenseAbilityComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<DecryptorsEyeSenseAbilityComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Menagerie.MOD_ID, "decryptors_eye_sense"), DecryptorsEyeSenseAbilityComponent.class);
    public final PlayerEntity player;
    private boolean senseEnabled = false;
    private int cooldownTicks = 0;
    private int activeTicks = 0;
    public DecryptorsEyeSenseAbilityComponent(PlayerEntity player) {
        this.player = player;
    }
    public void sync() { KEY.sync(this.player); }
    public boolean isSenseEnabled() { return senseEnabled; }
    public void setSenseEnabled(boolean senseEnabled) { this.senseEnabled = senseEnabled; }
    public int getCooldownTicks() { return cooldownTicks; }
    public int getActiveTicks() { return activeTicks; }
    public void setActiveTicks(int activeTicks) { this.activeTicks = activeTicks; }
    public void setCooldownTicks(int cooldownTicks) { this.cooldownTicks = cooldownTicks; }
    @Override
    public void tick() {
        if (cooldownTicks > 0) cooldownTicks--; activeTicks++;

    }
    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.senseEnabled = nbtCompound.getBoolean("senseEnabled");
        this.activeTicks = nbtCompound.getInt("activeTicks");
        this.cooldownTicks = nbtCompound.getInt("cooldownTicks");
    }
    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("senseEnabled", senseEnabled);
        nbtCompound.putInt("activeTicks", activeTicks);
        nbtCompound.putInt("cooldownTicks", cooldownTicks);
    }
}
