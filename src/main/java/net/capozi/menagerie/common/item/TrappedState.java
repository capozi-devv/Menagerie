package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.entity.object.ChainsEntity;

public interface TrappedState {
    boolean isTrapped();
    void setTrapped(boolean trapped);
    ChainsEntity getTrappedChains();
    void setTrappedChains(ChainsEntity chains);
}
