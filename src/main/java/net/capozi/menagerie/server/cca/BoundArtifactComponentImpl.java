package net.capozi.menagerie.server.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

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