package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.enchant.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EnchantInit {
    public static final Enchantment ARCANE_DAMAGE = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("menagerie", "arcane"),
            new ArcaneEnchantment()
    );
    public static final Enchantment POGO = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "pogo"),
            new PogoEnchantment()
    );
    public static final Enchantment CONDENSED = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "condensed"),
            new CondensedEnchantment()
    );
    public static final Enchantment IMPLOSION = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "implosion"),
            new ImplosionEnchantment()
    );
    public static final Enchantment BRACER = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(Menagerie.MOD_ID, "bracer"),
            new BracerEnchantment()
    );
    public static void init() {}
}
