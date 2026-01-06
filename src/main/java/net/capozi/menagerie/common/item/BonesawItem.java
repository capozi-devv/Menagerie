package net.capozi.menagerie.common.item;

import net.capozi.menagerie.foundation.DamageTypeInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class BonesawItem extends AxeItem {
    public BonesawItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient()) return;
        if (user instanceof PlayerEntity playerEntity) {
            if (!stack.isEmpty() && stack.isOf(ItemInit.BONESAW)) {
                if (remainingUseTicks > 1) {
                    Box box = playerEntity.getBoundingBox();
                    List<LivingEntity> livingEntities = world.getEntitiesByClass(LivingEntity.class, box.expand(0.5), e -> e instanceof LivingEntity);
                    for (LivingEntity entity : livingEntities) {
                        if (user.getBoundingBox().intersects(box.expand(0.5))) {
                            if (!(entity == playerEntity)) {
                                entity.damage(new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypeInit.BONESAW), playerEntity, playerEntity), 2);
                            }
                        }
                    }
                    playerEntity.getItemCooldownManager().set(this, 120);
                }
            }
        }
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000; // Same as bow
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient) {
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(this, 120);
            }
        }
    }
}
