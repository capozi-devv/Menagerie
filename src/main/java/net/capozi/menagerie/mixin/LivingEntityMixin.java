package net.capozi.menagerie.mixin;

import com.mojang.authlib.GameProfile;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.server.cca.BoundAccursedComponent;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract boolean teleport(double x, double y, double z, boolean particleEffects);
    private static final UUID SOUL_SPEED_BOOST_ID = UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038");
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDemiseDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((LivingEntity)(Object) this instanceof ServerPlayerEntity killed)) return;
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof ServerPlayerEntity killer)) return;

    }
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"), cancellable = true)
    private void onDieDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity)(Object)this;
        if (thisEntity instanceof PlayerEntity victimPlayer && source.getAttacker() instanceof ServerPlayerEntity killerPlayer) {
            if (victimPlayer.getWorld() == null) return;
            if (hasCameraItem(killerPlayer)) {
                cir.cancel();
                triggerMenagerieChainsEffect(killerPlayer, victimPlayer);
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
    private void triggerMenagerieChainsEffect(ServerPlayerEntity attacker, LivingEntity killed) {
        ServerWorld world = attacker.getServerWorld();
        ChainsEntity chains = new ChainsEntity(EntityInit.BLUE_CHAINS, killed.getWorld());
        killed.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 600, 1, false, false, true));
        Vec3d pos = killed.getPos();
        chains.refreshPositionAndAngles(BlockPos.ofFloored(pos), 0, 0);
        world.spawnEntity(chains);
        chains.setPos(pos.x, pos.y, pos.z);
        chains.startRiding(killed);
        killed.setHealth(20);
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
    @Inject(method = "addSoulSpeedBoostIfNeeded", at = @At("HEAD"), cancellable = true)
    private void addSoulSpeedIfUsingMarkOfDissonance(CallbackInfo ci) {
        if (((Object)this instanceof PlayerEntity marked)) {
            BoundArtifactComponent component = Menagerie.getBoundArtifact().get(marked);
            if (component != null) {
                if (component.hasArtifact()) {
                    if (((LivingEntity)(Object)this).isOnSoulSpeedBlock() || marked.getWorld().isNight()) {
                        EntityAttributeInstance entityAttributeInstance = (marked.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                        if (entityAttributeInstance == null) {
                            return;
                        }
                        ((LivingEntity)(Object)this).displaySoulSpeedEffects();
                        entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(SOUL_SPEED_BOOST_ID, "Soul speed boost", (double) (0.03F * (1.0F + (float) 3 * 0.35F)), EntityAttributeModifier.Operation.ADDITION));
                        ci.cancel();
                    }
                }
            }
        }
    }
}