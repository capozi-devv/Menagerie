package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.effect.ChainedEffect;
import net.capozi.menagerie.common.effect.RuinousEffect;
import net.capozi.menagerie.common.effect.VulnerabilityEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface EffectInit {
    StatusEffect CHAINED_EFFECT = new ChainedEffect();
    StatusEffect RUINOUS = new RuinousEffect();
    StatusEffect VULNERABILITY = new VulnerabilityEffect();
    RegistryEntry<StatusEffect> immobilized = registerStatusEffect("immobilize", CHAINED_EFFECT);
    RegistryEntry<StatusEffect> dismantled = registerStatusEffect("ruinous", RUINOUS);
    RegistryEntry<StatusEffect> vulnerability = registerStatusEffect("vulnerability", VULNERABILITY);
    static RegistryEntry<StatusEffect> registerStatusEffect(String name,StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Menagerie.MOD_ID, name), statusEffect);
    }
    static void registerEffects() {}
    StatusEffect[] negativeEffects = {
            StatusEffects.SLOWNESS,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.WEAKNESS,
            RUINOUS,
            StatusEffects.HUNGER,
            StatusEffects.BLINDNESS,
            StatusEffects.POISON,
            StatusEffects.WITHER,
            VULNERABILITY,
            StatusEffects.GLOWING,
            StatusEffects.NAUSEA
    };
    StatusEffect[] positiveEffects = {
            StatusEffects.SPEED,
            StatusEffects.HASTE,
            StatusEffects.STRENGTH,
            StatusEffects.ABSORPTION,
            StatusEffects.JUMP_BOOST,
            StatusEffects.NIGHT_VISION,
            StatusEffects.REGENERATION,
            StatusEffects.SATURATION,
            StatusEffects.RESISTANCE,
            StatusEffects.INVISIBILITY,
            StatusEffects.FIRE_RESISTANCE
    };
}
