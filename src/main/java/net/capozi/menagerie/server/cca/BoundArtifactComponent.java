package net.capozi.menagerie.server.cca;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.capozi.menagerie.Menagerie;
import net.minecraft.entity.player.PlayerEntity;

public interface BoundArtifactComponent extends AutoSyncedComponent {
    boolean hasArtifact();
    void setHasArtifact(boolean value);
    void tickArtifact(PlayerEntity player);
    static void sync(PlayerEntity player) {
        Menagerie.BOUND_ARTIFACT.sync(player);
    }
}

