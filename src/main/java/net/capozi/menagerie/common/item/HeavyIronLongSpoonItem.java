package net.capozi.menagerie.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HeavyIronLongSpoonItem extends SwordItem {
    public HeavyIronLongSpoonItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }
    @Override
    public int getItemBarColor(ItemStack stack) {
        return super.getItemBarColor(stack);
    }
    int useTicks = 0;
    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user.canHit()){
            user.getAttacking().takeKnockback((double)getMaxUseTime(stack) / 10d, -Math.sin(user.getYaw() * 0.017453292F), Math.cos(user.getPitch() * 0.017453292F));
        }
        useTicks = 0;
    }
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks < getMaxUseTime(stack)) {
            useTicks++;
        }
    }
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
}
