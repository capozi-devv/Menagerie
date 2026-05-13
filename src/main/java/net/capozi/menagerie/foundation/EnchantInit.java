package net.capozi.menagerie.foundation;

import devv.capozi.zip.common.index.Registrar;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.enchant.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface EnchantInit {
    Registrar<Enchantment> registrar = new Registrar<Enchantment>(Menagerie.MOD_ID, Registries.ENCHANTMENT);
    Enchantment ARCANE_DAMAGE = registrar.add(
            new Identifier("menagerie", "arcane"),
            new ArcaneEnchantment()
    );
    Enchantment POGO = registrar.add(
            new Identifier(Menagerie.MOD_ID, "pogo"),
            new PogoEnchantment()
    );
    Enchantment CONDENSED = registrar.add(
            new Identifier(Menagerie.MOD_ID, "condensed"),
            new CondensedEnchantment()
    );
    Enchantment IMPLOSION = registrar.add(
            new Identifier(Menagerie.MOD_ID, "implosion"),
            new ImplosionEnchantment()
    );
    Enchantment BRACER = registrar.add(
            new Identifier(Menagerie.MOD_ID, "bracer"),
            new BracerEnchantment()
    );
    Enchantment CHARGER = registrar.add(
            new Identifier(Menagerie.MOD_ID, "charger"),
            new ChargerEnchantment()
    );
    static void init() { registrar.setRegistries(registrar.entries, registrar.registry_consumer); }
}
