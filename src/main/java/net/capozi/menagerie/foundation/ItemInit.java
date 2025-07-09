package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import static net.capozi.menagerie.foundation.BlockInit.*;

public class ItemInit {
    public static void itemsRegistry() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemInit::registerItemToToolsTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemInit::registerBlocksToBuildingTab);
    }
    private static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Menagerie.MOD_ID, name), item);
    }
    public static void registerItemToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(DAMNATIO_MEMORIAE_MUSIC_DISC);
        entries.add(MARK_OF_DISSONANCE);
        entries.add(CAMERA_OF_THE_OTHERSIDE);
        entries.add(MARK_OF_THE_ACCURSED);
        entries.add(SIMULACRUM_VESSEL);
    }
    public static void registerBlocksToBuildingTab(FabricItemGroupEntries entries) {
        entries.add(CAPOZI_PLUSH);
        entries.add(EYA_PLUSH);
        entries.add(COSMO_PLUSH);
    }
    public static final Item MARK_OF_DISSONANCE = registerItems("mark_of_dissonance", new MarkOfDissonanceItem(new FabricItemSettings().maxCount(1)));
    public static final Item CAMERA_OF_THE_OTHERSIDE = registerItems("camera_of_the_otherside",new CameraOfTheOthersideItem(new FabricItemSettings().maxCount(1)));
    public static final Item MARK_OF_THE_ACCURSED = registerItems("mark_of_the_accursed", new MarkOfTheAccursedItem(new FabricItemSettings().maxCount(1)));
    public static final Item SIMULACRUM_VESSEL = registerItems("simulacrum_vessel", new SimulacrumVesselItem(new FabricItemSettings().maxCount(1)));
    public static final Item TEST_ITEM = registerItems("test_item", new TestItem(new FabricItemSettings()));
    public static final Item DAMNATIO_MEMORIAE_MUSIC_DISC = registerItems("damnatio_memoriae", new MusicDiscItem(7, SoundInit.DAMNATIO_MEMORIAE, new FabricItemSettings().maxCount(1), 83));
}
