package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.render.FlashOverlayRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (target instanceof ServerPlayerEntity targetPlayer && target instanceof TrappedState trappedState) {
            FlashOverlayRenderer.triggerFlash();
        }
        return ActionResult.SUCCESS;
    }
}
