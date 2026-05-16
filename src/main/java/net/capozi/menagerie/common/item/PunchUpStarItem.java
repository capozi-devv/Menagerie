package net.capozi.menagerie.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.capozi.menagerie.foundation.DamageTypeInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.capozi.menagerie.server.cca.PunchUpComboComponent;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PunchUpStarItem extends ToolItem {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public PunchUpStarItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 3d, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -1d, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }
    @Override
    public UseAction getUseAction(ItemStack stack) {
        int bracer = EnchantmentHelper.getLevel(EnchantInit.BRACER, stack);
        if (bracer > 0) {
            return UseAction.BLOCK;
        }
        return super.getUseAction(stack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        PunchUpComboComponent combo = PunchUpComboComponent.KEY.get(user);
        int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack);
        int bracer = EnchantmentHelper.getLevel(EnchantInit.BRACER, stack);
        int charger = EnchantmentHelper.getLevel(EnchantInit.CHARGER, stack);
        if (bracer > 0 || charger > 0) return ActionResult.PASS;
        user.setCurrentHand(hand);
        if (implosion > 0) {
            if (combo.getCombo() == 0) return ActionResult.PASS;
            user.swingHand(hand);
            user.getWorld().createExplosion(user, entity.getX(), entity.getY(), entity.getZ(), (float)(combo.getCombo() * 1.6), World.ExplosionSourceType.MOB);
            user.getWorld().playSound(null, user.getBlockPos(), SoundInit.COMBO_EXPLSOION, SoundCategory.PLAYERS, 1f, 1f);
            user.getItemCooldownManager().set(this, 150);
            combo.reset();
        } else if (combo.getCombo() > 0) {
            if (!user.getWorld().isClient) {
                double range = 4.5;
                Vec3d eyePos = user.getCameraPosVec(1.0f);
                Vec3d lookVec = user.getRotationVec(1.0f);
                Vec3d end = eyePos.add(lookVec.multiply(range));
                EntityHitResult hit = ProjectileUtil.raycast(
                        user,
                        eyePos,
                        end,
                        user.getBoundingBox().stretch(lookVec.multiply(range)).expand(1.0),
                        e -> !e.isSpectator() && e.isAlive(),
                        range*range
                );
                if (hit != null && hit.getEntity() != null) {
                    Entity target = hit.getEntity();
                    user.swingHand(hand);
                    target.damage(new DamageSource(user.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.PLAYER_ATTACK), user, user), combo.getCombo() * 2);
                    target.addVelocity((lookVec.getX() * (combo.getCombo() * 1.5)), (lookVec.getY() * (combo.getCombo() * 1.5)), (lookVec.getZ() * (combo.getCombo() * 1.5)));
                    user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundInit.COMBO_BOOST, SoundCategory.PLAYERS, 10f, 1.0F);
                    combo.reset();
                }
                user.getItemCooldownManager().set(this, 60);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, context.getStack());
        int bracer = EnchantmentHelper.getLevel(EnchantInit.BRACER, context.getStack());
        int charger = EnchantmentHelper.getLevel(EnchantInit.CHARGER, context.getStack());
        if (bracer > 0 || charger > 0) return ActionResult.PASS;
        if (implosion > 0) {
            PunchUpComboComponent combo = PunchUpComboComponent.KEY.get(context.getPlayer());
            if (combo.getCombo() == 0) return ActionResult.PASS;
            context.getPlayer().swingHand(context.getHand());
            context.getPlayer().getWorld().createExplosion(context.getPlayer(), context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), (float)(combo.getCombo()), World.ExplosionSourceType.MOB);
            context.getPlayer().getWorld().playSound(null, context.getPlayer().getBlockPos(), SoundInit.COMBO_EXPLSOION, SoundCategory.PLAYERS, 1f, 1f);
            context.getPlayer().getItemCooldownManager().set(this, 150);
            combo.reset();
        }
        return super.useOnBlock(context);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        PunchUpComboComponent combo = PunchUpComboComponent.KEY.get(user);
        user.setCurrentHand(hand);
        int bracer = EnchantmentHelper.getLevel(EnchantInit.BRACER, stack);
        if (bracer > 0) return  TypedActionResult.pass(stack);
        int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack);
        if (implosion > 0) return  TypedActionResult.pass(stack);
        int charger = EnchantmentHelper.getLevel(EnchantInit.CHARGER, stack);
        if (charger > 0) {
            if (combo.getCombo() > 0) {
                user.setVelocity(Vec3d.ZERO);
                dashPlayer(user, combo.getCombo());
                user.getWorld().playSound(null, user.getBlockPos(), SoundInit.COMBO_DASH, SoundCategory.PLAYERS, 1f, 1f);
                combo.reset();
                user.getItemCooldownManager().set(this, 150);
            }
        }
        user.swingHand(hand);
        return TypedActionResult.pass(stack);
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity != null && entity != stack.getHolder()) {
            stack.setHolder(entity);
        }
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            PunchUpComboComponent component = PunchUpComboComponent.KEY.get(player);
            target.damage(new DamageSource(player.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypeInit.PUNCH_COMBO)), (float)component.getCombo() * 1.5f);
            if (component.getCombo() < component.max_combo) {
                component.increment();
            }
        }
        return super.postHit(stack, target, attacker);
    }
    public static void dashPlayer(PlayerEntity player, double strength) {
        if (player == null) return;
        Vec3d look = player.getRotationVec(1.0F).normalize();
        Vec3d dashVelocity = new Vec3d(look.x * strength, look.y * strength, look.z * strength);
        player.addVelocity(dashVelocity.x, dashVelocity.y, dashVelocity.z);
        player.velocityModified = true;
    }
}
