package net.capozi.menagerie.common.item;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.network.UsageTickComponent;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HeavyIronLongSpoonItem extends ShovelItem {
    int usageTicks = 0;
    public HeavyIronLongSpoonItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (EnchantmentHelper.getLevel(EnchantInit.POGO, user.getStackInHand(hand)) > 0) return TypedActionResult.fail(user.getStackInHand(hand));
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
    @Override
    public int getItemBarStep(ItemStack stack) {
        UsageTickComponent comp = Menagerie.USAGE_TICKS.get(stack);
        int usage = comp == null ? 0 : comp.getTicks();
        return MathHelper.clamp(Math.round(13f * usage / 140f), 0, 13);
    }
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return EnchantmentHelper.getLevel(EnchantInit.POGO, stack) <= 0;
    }
    @Override
    public int getItemBarColor(ItemStack stack) {
        return EnchantmentHelper.getLevel(EnchantInit.CONDENSED, stack) != 1 ? 65531 : 16756224;
    }
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (EnchantmentHelper.getLevel(EnchantInit.POGO, context.getStack()) != 0) {
            if(((PlayerEntity)context.getPlayer()).canModifyBlocks()) {
                Vec3d lookVec = context.getPlayer().getRotationVec(1.0f);
                if (!context.getPlayer().isOnGround()) {
                    context.getPlayer().addVelocity(-lookVec.getX() * 1.15, -lookVec.getY() * 1.05, -lookVec.getZ() * 1.15);
                }
                context.getPlayer().addVelocity(-lookVec.getX(), -lookVec.getY(), -lookVec.getZ());
            }
            context.getWorld().playSound(null, context.getBlockPos(), SoundInit.POGO, SoundCategory.PLAYERS, 2f, 1f);
            context.getPlayer().getItemCooldownManager().set(this, 30);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (EnchantmentHelper.getLevel(EnchantInit.POGO, stack) > 0) return;
        if (user instanceof PlayerEntity playerEntity) {
            if (!stack.isEmpty() && stack.isOf(ItemInit.HEAVYIRON_LONGSPOON)) {
                int i = this.getMaxUseTime(stack) - getUseTicks();
                float f = getPullProgress(i);
                if (!((double)f < 0.1)) {
                    if (!world.isClient) {
                        double range = 4.5;
                        Vec3d eyePos = playerEntity.getCameraPosVec(1.0f);
                        Vec3d lookVec = playerEntity.getRotationVec(1.0f);
                        Vec3d end = eyePos.add(lookVec.multiply(range));
                        EntityHitResult hit = ProjectileUtil.raycast(
                                playerEntity,
                                eyePos,
                                end,
                                playerEntity.getBoundingBox().stretch(lookVec.multiply(range)).expand(1.0),
                                e -> !e.isSpectator() && e.isAlive(),
                                range*range
                        );
                        if (hit != null && hit.getEntity() != null) {
                            Entity target = hit.getEntity();
                            int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
                            target.damage(new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.PLAYER_ATTACK)), 10);
                            if (EnchantmentHelper.getLevel(EnchantInit.CONDENSED, stack) > 0) {
                                target.addVelocity((lookVec.getX() * useTime) / 7.5, (lookVec.getY() * useTime) / 3.25, (lookVec.getZ() * useTime) / 7.5);
                            } else {
                                target.addVelocity((lookVec.getX() * useTime) / 15, (lookVec.getY() * useTime) / 7.5, (lookVec.getZ() * useTime) / 15);
                            }
                            world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundInit.SPOON_BONK, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        }
                    }
                }
            }
        }
        usageTicks = 0;
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    public int getMaxUseTime(ItemStack stack) {
        return 76000;
    }
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (EnchantmentHelper.getLevel(EnchantInit.POGO, stack) > 0) return;
        usageTicks = usageTicks + 1;
        if (usageTicks >= getMaxUseTime(stack)) {
            usageTicks = getMaxUseTime(stack);
        }
    }
    public int getUseTicks() {
        return usageTicks;
    }
}
