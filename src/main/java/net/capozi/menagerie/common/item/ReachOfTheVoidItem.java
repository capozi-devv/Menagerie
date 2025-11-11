package net.capozi.menagerie.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
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
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ReachOfTheVoidItem extends Item {
    public ReachOfTheVoidItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof ServerPlayerEntity sUser) {
            if (entity instanceof ServerPlayerEntity target) {
                EnderChestInventory targetEnder = new EnderChestInventory() {
                    @Override
                    public int size() {
                        return target.getEnderChestInventory().size();
                    }
                    @Override
                    public boolean isEmpty() {
                        return target.getEnderChestInventory().isEmpty();
                    }
                    @Override
                    public ItemStack getStack(int slot) {
                        return target.getEnderChestInventory().getStack(slot);
                    }
                    @Override
                    public ItemStack removeStack(int slot, int amount) {
                        ItemStack result = target.getEnderChestInventory().removeStack(slot, amount);
                        if (!result.isEmpty()) {
                            sUser.closeHandledScreen();
                        }
                        return result;
                    }
                    @Override
                    public ItemStack removeStack(int slot) {
                        ItemStack result = target.getEnderChestInventory().removeStack(slot);
                        if (!result.isEmpty()) {
                            sUser.closeHandledScreen();
                            user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.BLOCKS);
                        }
                        return result;
                    }
                    @Override
                    public void setStack(int slot, ItemStack stack) {
                        target.getEnderChestInventory().setStack(slot, stack);
                    }
                    @Override
                    public void markDirty() {
                        target.getEnderChestInventory().markDirty();
                    }
                    @Override
                    public boolean canPlayerUse(PlayerEntity player) {
                        return target.getEnderChestInventory().canPlayerUse(player);
                    }
                    @Override
                    public void clear() {
                        target.getEnderChestInventory().clear();
                    }
                };
                user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
                sUser.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, targetEnder), Text.translatable("container.enderchest")));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
