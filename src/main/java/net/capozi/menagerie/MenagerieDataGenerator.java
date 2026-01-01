package net.capozi.menagerie;

import net.capozi.menagerie.common.datagen.AdvancementProvider;
import net.capozi.menagerie.common.datagen.ItemTagProvider;
import net.capozi.menagerie.common.datagen.LootTableProvider;
import net.capozi.menagerie.common.datagen.RecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MenagerieDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(RecipeProvider::new);
		pack.addProvider(LootTableProvider::new);
		pack.addProvider(ItemTagProvider::new);
        pack.addProvider(AdvancementProvider::new);
	}
}
