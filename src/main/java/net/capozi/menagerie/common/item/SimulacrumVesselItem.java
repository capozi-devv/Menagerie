package net.capozi.menagerie.common.item;

import com.mojang.datafixers.types.templates.Tag;
import net.capozi.menagerie.MenagerieConfig;
import net.capozi.menagerie.common.datagen.tags.AllItemTags;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

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
        if (MenagerieConfig.nonduplicative.contains(copyTargetStack.getItem().getRegistryEntry().registryKey().getValue().toString())) {
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
