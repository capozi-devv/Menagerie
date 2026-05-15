package net.capozi.menagerie.common.datagen;

import net.capozi.menagerie.common.datagen.tags.AllItemTags;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
                .add(ItemInit.DAMNATIO_MEMORIAE_MUSIC_DISC);
        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ItemInit.HEAVYIRON_LONGSPOON);
        getOrCreateTagBuilder(AllItemTags.CARNIVORE_EDIBLE)
                .add(Items.COOKED_BEEF)
                .add(Items.COOKED_CHICKEN)
                .add(Items.COOKED_COD)
                .add(Items.COOKED_MUTTON)
                .add(Items.COOKED_RABBIT)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.COOKED_SALMON)
                .add(Items.BEEF)
                .add(Items.CHICKEN)
                .add(Items.COD)
                .add(Items.MUTTON)
                .add(Items.RABBIT)
                .add(Items.PORKCHOP)
                .add(Items.SALMON)
                .add(Items.ROTTEN_FLESH)
                .add(Items.TROPICAL_FISH);
    }
}
