package net.capozi.menagerie;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.capozi.menagerie.common.datagen.LootTableModifiers;
import net.capozi.menagerie.foundation.*;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.server.cca.BoundAccursedComponent;
import net.capozi.menagerie.server.cca.BoundAqueousComponent;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menagerie implements ModInitializer {
	public static final String MOD_ID = "menagerie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier WARDEN_LOOT_TABLE_ID = new Identifier("minecraft", "entities/warden");
    public static ComponentKey<BoundArtifactComponent> BOUND_ARTIFACT;
    public static ComponentKey<BoundAccursedComponent> BOUND_ACCURSED;
    public static ComponentKey<BoundAqueousComponent> BOUND_AQUEOUS;
    public static ComponentKey<BoundArtifactComponent> getBoundArtifact() { return BOUND_ARTIFACT; }
    public static ComponentKey<BoundAccursedComponent> getBoundAccursed() { return BOUND_ACCURSED; }
    public static ComponentKey<BoundAqueousComponent> getBoundAqueous() { return BOUND_AQUEOUS; }
	@Override
	public void onInitialize() {
		ItemInit.registerItems();
		BlockInit.registerBlocks();
		SoundInit.registerSounds();
		EffectInit.registerEffects();
        EntityInit.register();
		EnchantInit.init();
        DamageTypeInit.init();
		LootTableModifiers.modifyLootTables();
        ParticleInit.PARTICLES.register();
		Registry.register(Registries.PAINTING_VARIANT, new Identifier(MOD_ID,"ether"), new PaintingVariant(64,48));
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
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (WARDEN_LOOT_TABLE_ID.equals(id)) {
				NbtList enchantments = new NbtList();
				NbtCompound enchantment = new NbtCompound();
				enchantment.putString("id", "menagerie:arcane");
				enchantment.putInt("lvl", 1);
				enchantments.add(enchantment);

				NbtCompound nbt = new NbtCompound();
				nbt.put("StoredEnchantments", enchantments);

				// Create loot pool entry for enchanted book
				LootPool.Builder pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.with(ItemEntry.builder(Items.ENCHANTED_BOOK)
								.apply(SetNbtLootFunction.builder(nbt))
								.weight(1))
						.conditionally(RandomChanceLootCondition.builder(0.25f)); // 25% chance
				tableBuilder.pool(pool);
			}
		});
    }
	public static Identifier identifier(String name) {
		return new Identifier(Menagerie.MOD_ID, name);
	}
}