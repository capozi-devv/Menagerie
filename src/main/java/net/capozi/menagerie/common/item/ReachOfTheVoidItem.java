package net.capozi.menagerie.common.item;

import net.capozi.menagerie.client.gui.ListeningInventory;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;

public class ReachOfTheVoidItem extends ToolItem {
    private final ToolMaterial material;
    public ReachOfTheVoidItem(ToolMaterial material, Settings settings) {
        super(material, settings);
        this.material = material;
    }
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof ServerPlayerEntity sUser) {
            if (entity instanceof ServerPlayerEntity target) {
                EnderChestInventory targetEnder = target.getEnderChestInventory();
                PlayerInventory listening = new ListeningInventory(user.getInventory(), () -> {
                    sUser.closeHandledScreen();
                    sUser.getWorld().playSound(null, sUser.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.BLOCKS);
                });
                for (int i = 0; i < targetEnder.size(); i++) {
                    ItemStack stack1 = targetEnder.getStack(i);
                    if (stack1.isOf(ItemInit.REACH_OF_THE_VOID)) {
                        stack1.damage(1, Random.create(), target);
                        if (stack1.getDamage() >= 25) {
                            stack1.decrement(1);
                            target.getWorld().playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 1f, 1f);
                            user.getItemCooldownManager().set(ItemInit.REACH_OF_THE_VOID, 6000);
                            return ActionResult.FAIL;
                        }
                        user.getItemCooldownManager().set(ItemInit.REACH_OF_THE_VOID, 900);
                        user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS);
                        return ActionResult.FAIL;
                    }
                }
                user.getWorld().playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS);
                sUser.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> GenericContainerScreenHandler.createGeneric9x3(syncId, listening, targetEnder), Text.literal(target.getName().getString() + "'s Ender Chest")));
                stack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }
    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.round(13 * (material.getDurability() - stack.getDamage()) / 25f);
    }
    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x008B8B;
    }
}
