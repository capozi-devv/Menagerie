package net.capozi.menagerie.common.item;

import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimulacrumVesselItem extends Item {
    public SimulacrumVesselItem(Settings settings) {
        super(settings);
    }
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        Item copyTarget = player.getMainHandStack().getItem();
        ItemStack copyTargetStack = player.getMainHandStack();
        BlockPos pos = player.getBlockPos();
        if (copyTarget instanceof SimulacrumVesselItem) {
            player.sendMessage(Text.literal("Simulacra cannot self-replicate"), true);
            return TypedActionResult.fail(copyTargetStack);
        } else if (copyTargetStack.getCount() > 1) {
            player.sendMessage(Text.literal("Simulacra cannot produce more than what they were made from"), true);
            return TypedActionResult.fail(copyTargetStack);
        } else {
            player.getOffHandStack().decrement(1);
            player.getInventory().setStack(40, copyTargetStack);
            player.getWorld().playSound(null, pos, SoundInit.SIMULACRA, SoundCategory.PLAYERS, 1f, 1f);
            return TypedActionResult.consume(copyTargetStack);
        }
    }
}
