package net.capozi.menagerie.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;


@Mixin(targets = {"net/minecraft/screen/PlayerScreenHandler$1"})
public abstract class PlayerScreenHandlerSlotMixin {
    @Inject(
            method = "canInsert",
            at = {@At("RETURN")},
            cancellable = true
    )
    private void menagerie$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Arrays.stream(banners).toList().contains(stack.getItem())) {
            cir.setReturnValue(true);
        }
    }
    private Item[] banners = new Item[] {
            Items.WHITE_BANNER, Items.ORANGE_BANNER, Items.MAGENTA_BANNER, Items.LIGHT_BLUE_BANNER,
            Items.YELLOW_BANNER, Items.LIME_BANNER, Items.PINK_BANNER, Items.GRAY_BANNER,
            Items.LIGHT_GRAY_BANNER, Items.CYAN_BANNER, Items.PURPLE_BANNER, Items.BLUE_BANNER,
            Items.BROWN_BANNER, Items.GREEN_BANNER, Items.RED_BANNER, Items.BLACK_BANNER
    };
}
