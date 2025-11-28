package net.capozi.menagerie.server.network;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface UsageTickComponent extends Component {
    int getTicks();
    void setTicks(int ticks);
}
