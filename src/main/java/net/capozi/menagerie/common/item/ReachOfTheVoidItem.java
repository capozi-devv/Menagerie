package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.gui.ListeningInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public class ReachOfTheVoidItem extends Item {
    public ReachOfTheVoidItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof ServerPlayerEntity sUser) {
            DefaultedList<ItemStack> prevInv = sUser.getInventory().main;
            if (entity instanceof ServerPlayerEntity target) {
                EnderChestInventory targetEnder = target.getEnderChestInventory();
                Inventory listening = new ListeningInventory(targetEnder, () -> {
                    sUser.closeHandledScreen();
                    sUser.getWorld().playSound(null, sUser.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.BLOCKS);
                });
                user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
                sUser.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, listening), Text.literal(target.getName().getString() + "'s Ender Chest")));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
