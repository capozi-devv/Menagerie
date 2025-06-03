package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.item.CameraOfTheOthersideItem;
import net.capozi.menagerie.common.item.MarkOfDissonanceItem;
import net.capozi.menagerie.common.item.TestItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemInit {
    public static void itemsRegistry() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemInit::registerItemToToolsTabItemGroup);
    }
    private static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Menagerie.MOD_ID, name), item);
    }
    public static void registerItemToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(MARK_OF_DISSONANCE);
        entries.add(CAMERA_OF_THE_OTHERSIDE);
        entries.add(TEST_ITEM);
    }
    public static final Item MARK_OF_DISSONANCE = registerItems("mark_of_dissonance", new MarkOfDissonanceItem(new FabricItemSettings().maxCount(1)));
    public static final Item CAMERA_OF_THE_OTHERSIDE = registerItems("camera_of_the_otherside",new CameraOfTheOthersideItem(new FabricItemSettings().maxCount(1)));
    public static final Item TEST_ITEM = registerItems("test_item", new TestItem(new FabricItemSettings()));
}
