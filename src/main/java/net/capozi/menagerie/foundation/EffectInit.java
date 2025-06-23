package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.effect.ChainedEffect;
import net.capozi.menagerie.common.effect.Etherot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class EffectInit extends StatusEffects {
    public static final StatusEffect CHAINED_EFFECT = new ChainedEffect();
    public static final StatusEffect ETHEROT = new Etherot();
    public static RegistryEntry<StatusEffect> IMMOBILIZED = registerStatusEffect("immobilize",
            CHAINED_EFFECT);
    public static final RegistryEntry<StatusEffect> ETHEROT_EFFECT = registerStatusEffect("etherot",
            ETHEROT);
    public static RegistryEntry<StatusEffect> registerStatusEffect(String name,StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Menagerie.MOD_ID, name),statusEffect);
    }
    public static void registerEffects() {

    }
}
