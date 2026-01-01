package net.capozi.menagerie.common.datagen;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public AdvancementProvider(FabricDataOutput output) {
        super(output);
    }
    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement menagerie = Advancement.Builder.create()
                .display(
                        ItemInit.CAMERA_OF_THE_OTHERSIDE, // The display icon
                        Text.literal("Menagerie"), // The title
                        Text.literal("The first steps into chaos"), // The description
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"), // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
                        AdvancementFrame.GOAL, // TASK, CHALLENGE, or GOAL
                        true, // Show the toast when completing it
                        true, // Announce it to chat
                        false // Hide it in the advancement tab until it's achieved
                )
                // "got_dirt" is the name referenced by other advancements when they want to have "requirements."
                .criterion("menagerie", InventoryChangedCriterion.Conditions.items((Item)ItemInit.MENAGERIE_ITEMS))
                // Give the advancement an id
                .build(consumer, Menagerie.MOD_ID + "/menagerie");
    }
}
