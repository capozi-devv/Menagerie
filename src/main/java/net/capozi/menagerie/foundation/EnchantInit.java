package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.enchant.ArcaneEnchantment;
import net.capozi.menagerie.common.enchant.CondensedEnchantment;
import net.capozi.menagerie.common.enchant.PogoEnchantment;
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
    public static void init() {}
}
