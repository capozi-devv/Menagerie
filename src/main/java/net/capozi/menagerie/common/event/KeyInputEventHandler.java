package net.capozi.menagerie.common.event;

import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.api.*;
import net.capozi.menagerie.common.entity.TrinketsHelper;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class KeyInputEventHandler {
    public static final String CATEGORY = "key.category.menagerie";
    public static final String EYE_ABILITY_KEY = "key.menagerie.decryptors_eye_ability";
    public static KeyBinding eyeKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(eyeKey.wasPressed()) {
                if (TrinketsHelper.findAllEquippedBy(client.player).contains(ItemInit.DECRYPTORS_EYE.getDefaultStack())) {

                }
            }
        });
    }

    public static void register() {
        eyeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                EYE_ABILITY_KEY,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                CATEGORY
        ));
        registerKeyInputs();
    }
}
