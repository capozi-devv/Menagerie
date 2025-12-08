package net.capozi.menagerie.mixin;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.server.network.BoundArtifactComponent;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "applyDamage", at = @At("TAIL"), cancellable = true)
    private void onLethalDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (!((Object) this instanceof ServerPlayerEntity killed)) return;
        float health = killed.getHealth();
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof ServerPlayerEntity killer)) return;
        if (hasCameraItem(killer) && health <= amount) {
            ci.cancel();
            triggerTotemEffect(killed); // heal and freeze logic
        }
    }
    private boolean hasCameraItem(ServerPlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(ItemInit.CAMERA_OF_THE_OTHERSIDE)) {
                return true;
            }
        }
        return false;
    }
    private void triggerTotemEffect(ServerPlayerEntity killed) {
        killed.setHealth(20.0F);
        killed.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 600, 1, false, false, true));
        killed.setVelocity(Vec3d.ZERO);
        killed.velocityModified = true;
        killed.getWorld().playSound(
                null, killed.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE,
                SoundCategory.PLAYERS, 15.0F, 1.0F
        );
    }
    @Inject(method = "damage", at = @At("TAIL"), cancellable = true)
    private void redirectToMagicDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!(source.getAttacker() instanceof LivingEntity attacker)) return;
        ItemStack stack = attacker.getMainHandStack();
        if (EnchantmentHelper.getLevel(EnchantInit.ARCANE_DAMAGE, stack) <= 0) return;
        LivingEntity target = (LivingEntity) (Object) this;
        DamageSources sources = target.getDamageSources();
        amount = amount * 0.2f;
        target.damage(sources.magic(), amount);
    }
}