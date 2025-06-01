package net.capozi.menagerie;

import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.foundation.BlockInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
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
		FabricDefaultAttributeRegistry.register(EntityInit.ABYSSAL_CHAINS, ChainsEntity.createChainAttributes());
	}
	public static Identifier identifier(String name) {
		return new Identifier(Menagerie.MOD_ID, name);
	}
}