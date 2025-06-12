package net.capozi.menagerie.foundation;

import net.capozi.menagerie.common.enchant.ArcaneEnchantment;
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
    public static void init() {

    }
}
