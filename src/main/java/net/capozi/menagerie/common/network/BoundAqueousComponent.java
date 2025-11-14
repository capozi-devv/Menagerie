package net.capozi.menagerie.common.network;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public interface BoundAqueousComponent extends Component {
    boolean hasAqueous();
    void setHasAqueous(boolean value);
    void tickAqueous(PlayerEntity player);
}
