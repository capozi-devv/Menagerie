package net.capozi.menagerie.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = Integer.MAX_VALUE)
public class InGameHudMixin {
    private static final Identifier BACKGROUND = Menagerie.identifier("textures/gui/hud/punch_up_crosshair_background.png");
    private static final Identifier DEFAULT = Menagerie.identifier("textures/gui/hud/punch_up_crosshair.png");
    private static final Identifier CHARGER = Menagerie.identifier("textures/gui/hud/punch_up_crosshair_charger.png");
    private static final Identifier IMPLOSION = Menagerie.identifier("textures/gui/hud/punch_up_crosshair_implosion.png");
    @Shadow @Final
    private MinecraftClient client;
    @Shadow
    private static Identifier ICONS;
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void menagerie$renderCrosshair(DrawContext context, CallbackInfo ci) {
        if (!client.options.getPerspective().isFirstPerson()) return;
        ClientPlayerEntity player = this.client.player;
        if (player == null) return;
        ItemCooldownManager manager = player.getItemCooldownManager();
        if (player.getMainHandStack().isOf(ItemInit.PUNCH_UP_STAR)) {
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
            );
            ItemStack stack = player.getMainHandStack();
            int j = (context.getScaledWindowHeight() / 2) - 7;
            int k = (context.getScaledWindowWidth() / 2) - 7;
            context.drawTexture(BACKGROUND, k, j, 0, 0, 15, 15);
            if (stack.hasEnchantments()) {
                int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack);
                int charger = EnchantmentHelper.getLevel(EnchantInit.CHARGER, stack);
            }
            if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
                float f = this.client.player.getAttackCooldownProgress(0.0F);
                boolean bl = false;
                if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && f >= 1.0F) {
                    bl = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                    bl &= this.client.targetedEntity.isAlive();
                }

                int m = context.getScaledWindowHeight() / 2 - 7 + 16;
                int n = context.getScaledWindowWidth() / 2 - 8;
                if (bl) {
                    context.drawTexture(ICONS, n, m, 68, 94, 16, 16);
                } else if (f < 1.0F) {
                    int l = (int)(f * 17.0F);
                    context.drawTexture(ICONS, n, m, 36, 94, 16, 4);
                    context.drawTexture(ICONS, n, m, 52, 94, l, 4);
                }
            }
            RenderSystem.defaultBlendFunc();
            ci.cancel();
        }
    }
}
