package net.capozi.menagerie.common.network;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;

public interface EtherotComponent extends Component {
    boolean hasEtherot();
    void setHasEtherot(boolean hasEtherot);
    void tickEtherot(PlayerEntity player);
}
