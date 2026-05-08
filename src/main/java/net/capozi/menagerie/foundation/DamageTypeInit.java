package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public interface DamageTypeInit {
    static void init() {}
    RegistryKey<DamageType> BONESAW = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Menagerie.MOD_ID, "bonesaw"));
    RegistryKey<DamageType> PUNCH_COMBO = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Menagerie.MOD_ID, "punch_combo"));
}
