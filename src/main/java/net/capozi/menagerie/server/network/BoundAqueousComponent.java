package net.capozi.menagerie.server.network;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;

public interface BoundAqueousComponent extends Component {
    boolean hasAqueous();
    void setHasAqueous(boolean value);
    void tickAqueous(PlayerEntity player);
}
