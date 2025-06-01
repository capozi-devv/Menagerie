package net.capozi.menagerie.common.item;

import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        BlockPos playerPos = user.getBlockPos();
        if (!user.getWorld().isClient && user instanceof ServerPlayerEntity serverPlayer) {
            user.getWorld().playSound(null, playerPos, SoundInit.BUTTON_CLICK,
                    SoundCategory.MASTER, 5f, 1f);
            user.getWorld().playSound(null, playerPos, SoundInit.FILM_ADVANCE_LAST,
                    SoundCategory.MASTER, 5f, 1f);
        }
        return ActionResult.SUCCESS;
    }
}
