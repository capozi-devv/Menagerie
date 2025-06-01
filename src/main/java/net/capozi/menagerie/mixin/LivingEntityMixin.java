package net.capozi.menagerie.mixin;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.item.TrappedState;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.common.item.CameraOfTheOthersideItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onLethalDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof ServerPlayerEntity killed)) return;
        float health = killed.getHealth();
        if (health > amount) return;
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof ServerPlayerEntity killer)) return;
        if (!hasCameraItem(killer)) return;
        cir.setReturnValue(false); // cancel actual death
        triggerTotemEffect(killed); // Heal and freeze
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
        killed.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20000, 255, false, false, false));
        killed.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20000, 128, false, false, false));
        killed.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20000, 255, false, false, false));
        // Cancel motion and pushing
        killed.setVelocity(Vec3d.ZERO);
        killed.velocityModified = true;
        //Play kill sound
        killed.getWorld().playSound(
                null, killed.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE,
                SoundCategory.PLAYERS, 15.0F, 1.0F
        );
    }
}