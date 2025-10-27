package net.capozi.menagerie.mixin;

import net.capozi.menagerie.foundation.EffectInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    private static boolean isComatose(@Nullable LivingEntity entity) {
        return entity != null && entity.hasStatusEffect(EffectInit.CHAINED_EFFECT) && !entity.isSpectator();
    }
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo callbackInfo) {
        if (isComatose(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
            KeyBinding.unpressAll();
            callbackInfo.cancel();
        }
    }
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
        if (isComatose(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
            KeyBinding.unpressAll();
            callbackInfo.cancel();
        }
    }
}