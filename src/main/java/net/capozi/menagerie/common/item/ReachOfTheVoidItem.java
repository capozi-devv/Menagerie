package net.capozi.menagerie.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
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
                EnderChestInventory targetEnder = target.getEnderChestInventory();
                sUser.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, targetEnder), Text.translatable("container.enderchest")));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }
}
