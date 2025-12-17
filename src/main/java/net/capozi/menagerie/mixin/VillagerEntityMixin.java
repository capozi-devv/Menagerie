package net.capozi.menagerie.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
//    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
//    private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
//        if (!player.getWorld().isClient) {
//            BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
//            if (accursed.hasAccursed()) {
//                cir.cancel();
//                cir.setReturnValue(ActionResult.FAIL);
//            }
//        }
//    }
}