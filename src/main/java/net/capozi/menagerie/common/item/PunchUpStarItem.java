package net.capozi.menagerie.common.item;

import net.capozi.menagerie.foundation.DamageTypeInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.capozi.menagerie.server.cca.PunchUpComboComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
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

public class PunchUpStarItem extends AxeItem {
    public PunchUpStarItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
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
        int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack);
        if (implosion > 0) {
            PunchUpComboComponent combo = PunchUpComboComponent.KEY.get(user);
            user.getWorld().createExplosion(user, entity.getX(), entity.getY(), entity.getZ(), (float)(combo.getCombo() * 1.6), World.ExplosionSourceType.MOB);
            combo.reset();
        }
        return super.useOnEntity(stack, user, entity, hand);
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
        if (bracer > 0) return  TypedActionResult.consume(stack);
        int implosion = EnchantmentHelper.getLevel(EnchantInit.IMPLOSION, stack);
        if (implosion > 0) return  TypedActionResult.consume(stack);
        int charger = EnchantmentHelper.getLevel(EnchantInit.CHARGER, stack);
        if (charger > 0) {
            if (combo.getCombo() > 0) {
                user.setVelocity(Vec3d.ZERO);
                dashPlayer(user, combo.getCombo());
                combo.reset();
                user.getItemCooldownManager().set(this, 300);
            }
        }
        return TypedActionResult.consume(stack);
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
            target.damage(new DamageSource(player.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypeInit.PUNCH_COMBO)), (float)component.getCombo());
            if (component.getCombo() < component.max_combo) {
                component.increment();
            }
            player.getWorld().playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 1f, (float)component.getCombo());
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
