package net.capozi.menagerie.server.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.EnchantInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;

public class PunchUpComboComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final Vec3i implosionColor = new Vec3i(220, 20, 60);
    private final Vec3i chargerColor = new Vec3i(222, 183, 29);
    private final Vec3i defaultColor = new Vec3i(15, 225, 155);
    public static final ComponentKey<PunchUpComboComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Menagerie.MOD_ID, "punch_up_combo"), PunchUpComboComponent.class);
    public final PlayerEntity player;
    public PunchUpComboComponent(PlayerEntity player) {
        this.player = player;
    }
    private int combo = 0;
    public final int max_combo = 10;
    public int getCombo() { sync(); return combo; }
    public void increment() { combo++; sync(); }
    public void reset() { combo = 0; sync(); }
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
    public int getChargeTint(ItemStack stack) {
        sync();
        Vec3i color;
        float percent;
        if (EnchantmentHelper.getLevel(EnchantInit.CHARGER, stack) > 0) {
            color = chargerColor;
            percent = Math.min(this.combo / this.max_combo, 1.0F);
        } else if (EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack) > 0) {
            color = implosionColor;
            percent = Math.min(this.combo / this.max_combo, 0.5F) * 2.0F;
        } else {
            color = defaultColor;
            percent = Math.min(this.combo / this.max_combo, 1.0F);
        }
        percent = Math.max(0.0F, percent);
        int r = (int)(255.0F - percent * (float)(255 - color.getX()));
        int g = (int)(255.0F - percent * (float)(255 - color.getY()));
        int b = (int)(255.0F - percent * (float)(255 - color.getZ()));
        sync();
        return r << 16 | g << 8 | b;
    }
}
