package net.capozi.menagerie.mixin;

import net.capozi.menagerie.foundation.EffectInit;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (MinecraftClient.getInstance().player != null) {
            var client = MinecraftClient.getInstance();
            if (client.player != null && client.player.hasStatusEffect(EffectInit.CHAINED_EFFECT)) {
                if (key == GLFW.GLFW_KEY_ESCAPE) return;
                if (action == GLFW.GLFW_RELEASE) return;
                callbackInfo.cancel();
            }
        }
    }
}
