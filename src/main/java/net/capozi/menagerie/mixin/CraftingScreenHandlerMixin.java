package net.capozi.menagerie.mixin;

import net.capozi.menagerie.common.item.TrickRoomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void menagerie$updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci) {
        for (ItemStack stack : craftingInventory.getInputStacks()) {
            if (stack.getItem() instanceof TrickRoomItem) {
                boolean bl = stack.getNbt().getBoolean("temporary");
                if (bl) {
                    resultInventory.setStack(0, new ItemStack(Items.AIR));
                }
            }
        }
    }
}
