package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundInit {
    public static void registerSounds() {
        Menagerie.LOGGER.info("Registered sounds");
    }
    private static SoundEvent registerSoundEvents(String name) {
        Identifier id = new Identifier(Menagerie.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static final SoundEvent BUTTON_CLICK = registerSoundEvents("button_click");
    public static final SoundEvent FILM_ADVANCE_LAST = registerSoundEvents("film_advance_last");
    public static final SoundEvent FLASH = registerSoundEvents("flash");
}
