package net.capozi.menagerie.common.network;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;

public interface BoundArtifactComponent extends Component {
    boolean hasArtifact();
    void setHasArtifact(boolean value);
    void tickArtifact(PlayerEntity player);

}

