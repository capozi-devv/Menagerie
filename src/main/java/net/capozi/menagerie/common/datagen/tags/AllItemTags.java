package net.capozi.menagerie.common.datagen.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AllItemTags {
    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(id));
    }
    public static final TagKey<Item> CARNIVORE_EDIBLE = of("carnivore_edible");
}
