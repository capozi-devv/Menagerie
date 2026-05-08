package net.capozi.menagerie.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = Integer.MAX_VALUE)
public class InGameHudMixin {
    private static final Identifier BACKGROUND = Menagerie.identifier("textures/gui/hud/punch_up_crosshair_background");
    private static final Identifier DEFAULT = Menagerie.identifier("textures/gui/hud/punch_up_crosshair");
    private static final Identifier CHARGER = Menagerie.identifier("textures/gui/hud/punch_up_crosshair_charger");
    private static final Identifier IMPLOSION = Menagerie.identifier("textures/gui/hud/punch_up_crosshair_implosion");
    @Shadow
    @Final
    private MinecraftClient client;
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void menagerie$renderCrosshair(DrawContext context, CallbackInfo ci) {
        if (!client.options.getPerspective().isFirstPerson()) return;
        ClientPlayerEntity player = this.client.player;
        if (player == null) return;
        ItemCooldownManager manager = player.getItemCooldownManager();
        if (player.getMainHandStack().isOf(ItemInit.PUNCH_UP_STAR)) {
            ItemStack stack = player.getMainHandStack();
            int j = (context.getScaledWindowHeight() / 2) - 7;
            int k = (context.getScaledWindowWidth() / 2) - 7;
            context.drawTexture(BACKGROUND, k, j, 0, 0, 15, 15);
            if (stack.hasEnchantments()) {
                int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack);
                int charger = EnchantmentHelper.getLevel(EnchantInit.CHARGER, stack);
            }
            ci.cancel();
        }
    }
}
