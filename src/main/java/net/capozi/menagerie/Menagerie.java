package net.capozi.menagerie;

import net.capozi.menagerie.foundation.*;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menagerie implements ModInitializer {
	public static final String MOD_ID = "menagerie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ItemInit.itemsRegistry();
		BlockInit.registerBlocks();
		SoundInit.registerSounds();
		EffectInit.registerEffects();
		FabricDefaultAttributeRegistry.register(EntityInit.ABYSSAL_CHAINS, ChainsEntity.createChainAttributes());
		AttackEntityCallback.EVENT.register((PlayerEntity player, World world, net.minecraft.util.Hand hand, Entity target, EntityHitResult hitResult) -> {
			StatusEffectInstance effect = player.getStatusEffect(EffectInit.CHAINED_EFFECT);
			if (effect != null && effect.getAmplifier() >= 0) {
				return ActionResult.FAIL; // Cancel attack
			}
			return ActionResult.PASS; // Allow normal behavior
		});
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((LivingEntity entity, DamageSource source, float amount) -> {
			if (entity instanceof PlayerEntity player) {
				StatusEffectInstance effect = player.getStatusEffect(EffectInit.CHAINED_EFFECT);
				return effect == null || effect.getAmplifier() < 0; // Cancel the damage
			}
			return true; // Allow normal damage
		});
	}
	public static Identifier identifier(String name) {
		return new Identifier(Menagerie.MOD_ID, name);
	}
}