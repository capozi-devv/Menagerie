package net.capozi.menagerie.server.network;

import net.minecraft.nbt.NbtCompound;

public class UsageTickComponentImpl implements UsageTickComponent {
    private int ticks = 0;

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    // Persist to NBT
    @Override
    public void readFromNbt(NbtCompound tag) {
        ticks = tag.getInt("UsageTicks");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("UsageTicks", ticks);
    }
}
