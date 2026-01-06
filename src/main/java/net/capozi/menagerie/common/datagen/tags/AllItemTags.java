package net.capozi.menagerie.common.datagen.tags;

import net.capozi.menagerie.Menagerie;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AllItemTags {
    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(Menagerie.MOD_ID, id));
    }
    public static final TagKey<Item> CARNIVORE_EDIBLE = of("carnivore_edible");
    public static final TagKey<Item> NONDUPLICATIVE = of("nonduplicative");
}
