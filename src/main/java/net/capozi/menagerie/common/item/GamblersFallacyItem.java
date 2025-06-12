package net.capozi.menagerie.common.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GamblersFallacyItem extends Item {
    public GamblersFallacyItem(Settings settings) { super(settings); }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            int result = user.getRandom().nextInt(2); // 0 or 1
            if (result == 0) {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 18000, 1));
            } else {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 18000, 1));
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}
