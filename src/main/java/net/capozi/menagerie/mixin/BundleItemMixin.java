package net.capozi.menagerie.mixin;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void menagerie$use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (user.isSneaking()) {
            TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(user).isPresent() ? TrinketsApi.getTrinketComponent(user).get() : null;
            if (!trinketComponent.isEquipped(Items.BUNDLE)) {
                TrinketItem.equipItem(user, user.getStackInHand(hand));
                cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
                cir.cancel();
            }
        }
    }
}
