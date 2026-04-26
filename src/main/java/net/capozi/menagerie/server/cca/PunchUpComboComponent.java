package net.capozi.menagerie.server.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.capozi.menagerie.Menagerie;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class PunchUpComboComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<PunchUpComboComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Menagerie.MOD_ID, "punch_up_combo"), PunchUpComboComponent.class);
    public final PlayerEntity player;
    public PunchUpComboComponent(PlayerEntity player) {
        this.player = player;
    }
    private int combo = 0;
    public int getCombo() { return combo; }
    public void increment() { combo++; }
    public void reset() { combo = 0; }
    public void sync() { KEY.sync(this.player); }
    @Override
    public void tick() {
        sync();
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        tag.getInt("combo");
    }
    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("combo", combo);
    }
}
