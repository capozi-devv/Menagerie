package net.capozi.menagerie.mixin;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.network.BoundAccursedComponent;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!player.getWorld().isClient) {
            BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
            if (accursed.hasAccursed()) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}