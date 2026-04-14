package net.capozi.menagerie.mixin;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getSwiftSneakSpeedBoost", at = @At("HEAD"), cancellable = true)
    private static void menagerie$getSwiftSneakSpeedBoost(LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        if ((entity instanceof PlayerEntity marked)) {
            BoundArtifactComponent component = Menagerie.getBoundArtifact().get(marked);
            if (component.hasArtifact()) {
                cir.setReturnValue(2.8f);
            }
        }
    }
}
