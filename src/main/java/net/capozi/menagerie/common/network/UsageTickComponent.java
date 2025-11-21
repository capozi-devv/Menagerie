package net.capozi.menagerie.common.network;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface UsageTickComponent extends Component {
    int getTicks();
    void setTicks(int ticks);
}
