package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.datagen.tags.AllItemTags;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
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
        Item copyTarget = player.getOffHandStack().getItem();
        ItemStack copyTargetStack = player.getOffHandStack();
        BlockPos pos = player.getBlockPos();
        if (copyTarget instanceof SimulacrumVesselItem) {
            player.sendMessage(Text.literal("Simulacra cannot self-replicate"), true);
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        if (copyTargetStack.getCount() > 1) {
            player.sendMessage(Text.literal("Simulacra cannot produce more than one object"), true);
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        if (copyTargetStack.isOf(Items.BUNDLE)
                || copyTargetStack.isOf(Items.SHULKER_BOX)
                || copyTargetStack.isOf(Items.ELYTRA)
                || copyTargetStack.isIn(ItemTags.TRIMMABLE_ARMOR)
                || copyTargetStack.isIn(ItemTags.TRIM_TEMPLATES)
                || copyTargetStack.isIn(ItemTags.TRIM_MATERIALS)
                || copyTargetStack.isIn(AllItemTags.NONDUPLICATIVE)
                || copyTargetStack.isIn(ItemTags.TOOLS)
                || copyTargetStack.isOf(Items.ENCHANTED_BOOK)) {
            player.sendMessage(Text.literal("Simulacra cannot produce this"), true);
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        if (!world.isClient()){
            world.spawnEntity(new ItemEntity(world, player.getX(), player.getY() + 1.0, player.getZ(), copyTargetStack.copy()));
            world.playSound(null, pos, SoundInit.SIMULACRA, SoundCategory.PLAYERS, 1f, 1f);
            player.getMainHandStack().decrement(1);
            return TypedActionResult.consume(player.getMainHandStack());
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}
