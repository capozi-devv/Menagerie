package net.capozi.menagerie.mixin;

import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
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
        if (!((LivingEntity)(Object) this instanceof ServerPlayerEntity killed)) return;
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof ServerPlayerEntity killer)) return;

    }
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"), cancellable = true)
    private void onDeathDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity)(Object)this;
        if (thisEntity instanceof PlayerEntity victimPlayer && source.getAttacker() instanceof ServerPlayerEntity killerPlayer) {
            if (victimPlayer.getWorld() == null) return;
            if (hasCameraItem(killerPlayer)) {
                cir.cancel();
                triggerChainEffect(killerPlayer, victimPlayer);
            }
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
    private void triggerChainEffect(ServerPlayerEntity attacker, LivingEntity killed) {
        ServerWorld world = attacker.getServerWorld();
        ChainsEntity chains = new ChainsEntity(EntityInit.ABYSSAL_CHAINS, killed.getWorld());
        BlockPos pos = killed.getBlockPos();
        chains.refreshPositionAndAngles(pos, 0, 0);
        world.spawnEntity(chains);
        killed.startRiding(chains, true);
        killed.setHealth(20);
        killed.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 600, 1, false, false, true));
        killed.getWorld().playSound(null, killed.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 15.0F, 1.0F);
    }
    @Inject(method = "applyDamage", at = @At("TAIL"), cancellable = true)
    private void redirectToMagicDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (!(source.getAttacker() instanceof LivingEntity attacker)) return;
        ItemStack stack = attacker.getMainHandStack();
        if (EnchantmentHelper.getLevel(EnchantInit.ARCANE_DAMAGE, stack) <= 0) return;
        LivingEntity target = (LivingEntity) (Object) this;
        DamageSources sources = target.getDamageSources();
        amount *= 0.2f;
        target.damage(sources.magic(), amount);
    }
}