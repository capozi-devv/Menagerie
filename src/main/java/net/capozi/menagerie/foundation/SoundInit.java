package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public interface SoundInit {
    static void registerSounds() {}
    private static SoundEvent registerSoundEvents(String name) {
        Identifier id = new Identifier(Menagerie.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    SoundEvent BUTTON_CLICK = registerSoundEvents("button_click");
    SoundEvent FILM_ADVANCE_LAST = registerSoundEvents("film_advance_last");
    SoundEvent FLASH = registerSoundEvents("flash");
    SoundEvent SIMULACRA = registerSoundEvents("simulacra");
    SoundEvent SPOON_BONK = registerSoundEvents("spoon_bonk");
    SoundEvent POGO = registerSoundEvents("pogo");
    SoundEvent REVIVAL = registerSoundEvents("revival");
    SoundEvent DAMNATIO_MEMORIAE = registerSoundEvents("damnatio_memoriae");
    SoundEvent SPOON_HIT = registerSoundEvents("spoon_hit");
    SoundEvent DECRYPTORS_EYE = registerSoundEvents("decryptors_eye");
}
