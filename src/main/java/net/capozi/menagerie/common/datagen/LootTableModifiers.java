package net.capozi.menagerie.common.datagen;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class LootTableModifiers {
    private static final Identifier FISHING_ID = new Identifier("minecraft","gameplay/fishing/treasure");
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, lootTableSource) -> {
            if(FISHING_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.005f))
                        .with(ItemEntry.builder(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .with(ItemEntry.builder(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 1f)).build());
                builder.pool(poolBuilder);
            }
        });
    }
}
