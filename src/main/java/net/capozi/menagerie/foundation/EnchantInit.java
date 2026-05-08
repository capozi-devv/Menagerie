package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.enchant.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface EnchantInit {
    Enchantment ARCANE_DAMAGE = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("menagerie", "arcane"),
            new ArcaneEnchantment()
    );
    Enchantment POGO = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "pogo"),
            new PogoEnchantment()
    );
    Enchantment CONDENSED = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "condensed"),
            new CondensedEnchantment()
    );
    Enchantment IMPLOSION = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "implosion"),
            new ImplosionEnchantment()
    );
    Enchantment BRACER = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "bracer"),
            new BracerEnchantment()
    );
    Enchantment CHARGER = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "charger"),
            new ChargerEnchantment()
    );
    static void init() {}
}
