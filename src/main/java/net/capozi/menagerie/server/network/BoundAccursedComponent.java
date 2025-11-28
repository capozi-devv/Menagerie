package net.capozi.menagerie.server.network;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;

public interface BoundAccursedComponent extends Component {
    boolean hasAccursed();
    void setHasAccursed(boolean hasAccursed);
    void tickAccursed(PlayerEntity player);
}
