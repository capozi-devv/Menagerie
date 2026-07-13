package net.capozi.menagerie.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class HeavyIronLongSpoonItem extends ShovelItem {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    protected static final UUID ATTACK_REACH_MODIFIER_ID = UUID.fromString("76a8dee3-3e7e-4e11-ba46-a19b0c724567");
    protected static final UUID REACH_MODIFIER_ID = UUID.fromString("a31c8afc-a716-425d-89cd-0d373380e6e7");
    int usageTicks = 0;
    public HeavyIronLongSpoonItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 10d, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.2d, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(ATTACK_REACH_MODIFIER_ID, "Weapon modifier", 1.5D, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_MODIFIER_ID, "Weapon modifier", 1.5D, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (EnchantmentHelper.getLevel(EnchantInit.POGO, user.getStackInHand(hand)) > 0) return TypedActionResult.fail(user.getStackInHand(hand));
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        player.getWorld().playSound(null, player.getBlockPos(), SoundInit.SPOON_HIT, SoundCategory.PLAYERS, 1f, 1f);
        return super.onLeftClickEntity(stack, player, entity);
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
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
                    context.getPlayer().setVelocity(-lookVec.getX() * 2.45, -lookVec.getY() * 2.35, -lookVec.getZ() * 2.45);
                } else {
                    context.getPlayer().setVelocity(-lookVec.getX() * 1.25, -lookVec.getY() * 1.25, -lookVec.getZ() * 1.25);
                }
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
                            if (!(target instanceof TntMinecartEntity)) {
                                target.damage(new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.PLAYER_ATTACK), playerEntity, playerEntity), 10);
                            }
                            if (EnchantmentHelper.getLevel(EnchantInit.CONDENSED, stack) > 0) {
                                target.setVelocity((lookVec.getX() * useTime) / 7.5, (lookVec.getY() * useTime) / 7.5, (lookVec.getZ() * useTime) / 7.5);
                            } else {
                                target.setVelocity((lookVec.getX() * useTime) / 15, (lookVec.getY() * useTime) / 15, (lookVec.getZ() * useTime) / 15);
                            }
                            world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundInit.SPOON_BONK, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        }
                        playerEntity.getItemCooldownManager().set(this, 60);
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
