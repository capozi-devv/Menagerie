package net.capozi.menagerie.foundation;

import devv.capozi.zip.common.index.Registrar;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.capozi.menagerie.foundation.BlockInit.*;

public class ItemInit {
    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemInit::registerItemToToolsTabItemGroup);
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemInit::registerBlocksToBuildingTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ItemInit::registerItemsToWeaponGroup);
        itemRegistrar.setRegistries(itemRegistrar.entries, itemRegistrar.registry_consumer);
    }
    public static void registerItemsToWeaponGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.TRIDENT, HEAVYIRON_LONGSPOON);
        entries.addAfter(HEAVYIRON_LONGSPOON, BONESAW);
    }
    public static void registerItemToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MUSIC_DISC_RELIC, DAMNATIO_MEMORIAE_MUSIC_DISC);
        entries.addAfter(DAMNATIO_MEMORIAE_MUSIC_DISC, CAMERA_OF_THE_OTHERSIDE);
        entries.addAfter(CAMERA_OF_THE_OTHERSIDE, SIMULACRUM_VESSEL);
        entries.addAfter(SIMULACRUM_VESSEL, REACH_OF_THE_VOID);
        entries.addAfter(REACH_OF_THE_VOID, ASTRAL_TEAR);
        entries.addAfter(ASTRAL_TEAR, TRICK_ROOM);
        entries.addAfter(TRICK_ROOM, MARK_OF_DISSONANCE);
        entries.addAfter(MARK_OF_DISSONANCE, MARK_OF_THE_ACCURSED);
        entries.addAfter(MARK_OF_THE_ACCURSED, MARK_OF_AQUEOUSNESS);
    }
//    public static void registerBlocksToBuildingTab(FabricItemGroupEntries entries) {
//        entries.add(CAPOZI_PLUSH);
//        entries.add(EYA_PLUSH);
//        entries.add(COSMO_PLUSH);
//    }
    private static Registrar<Item> itemRegistrar = new Registrar<Item>(Menagerie.MOD_ID, Registries.ITEM);
    public static final Item CAMERA_OF_THE_OTHERSIDE = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "camera_of_the_otherside"), new CameraOfTheOthersideItem(new FabricItemSettings().maxCount(1)));
    public static final Item SIMULACRUM_VESSEL = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "simulacrum_vessel"), new SimulacrumVesselItem(new FabricItemSettings().maxCount(16)));
    public static final Item TEST_ITEM = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "test_item"), new TestItem(new FabricItemSettings()));
    public static final Item DAMNATIO_MEMORIAE_MUSIC_DISC = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "damnatio_memoriae"), new MusicDiscItem(7, SoundInit.DAMNATIO_MEMORIAE, new FabricItemSettings().maxCount(1), 83));
    public static final Item HEAVYIRON_LONGSPOON = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "heavyiron_longspoon"), new HeavyIronLongSpoonItem(ToolMaterials.NETHERITE, 6, -3.2F, new Item.Settings().maxCount(1).fireproof()));
    public static final Item REACH_OF_THE_VOID = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "reach_of_the_void"), new ReachOfTheVoidItem(new Item.Settings().maxCount(1)));
    public static final Item INCOMPLETE_CONSTRUCT = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "incomplete_construct"), new Item(new Item.Settings()));
    public static final Item TRICK_ROOM = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "trick_room"), new TrickRoomItem(new Item.Settings().maxCount(1)));
    public static final Item MARK_OF_DISSONANCE = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_of_dissonance"), new MarkOfDissonanceItem(new Item.Settings().maxCount(1)));
    public static final Item MARK_OF_THE_ACCURSED = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_of_the_accursed"), new MarkOfTheAccursedItem(new Item.Settings().maxCount(1)));
    public static final Item MARK_OF_AQUEOUSNESS = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_of_aqueousness"), new MarkOfAqueousnessItem(new Item.Settings().maxCount(1)));
    public static final Item ASTRAL_TEAR = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "astral_tear"), new AstralTearItem(new Item.Settings().maxCount(1)));
    public static final Item BONESAW = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "bonesaw"), new BonesawItem(ToolMaterials.NETHERITE, 4, -2.8f, new  Item.Settings().maxCount(1)));
    public static final Item MARK_REMOVER = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_remover"), new MarkRemoverItem(new Item.Settings()));
    public static final List<Item> MENAGERIE_ITEMS = List.of(CAMERA_OF_THE_OTHERSIDE, SIMULACRUM_VESSEL, DAMNATIO_MEMORIAE_MUSIC_DISC, HEAVYIRON_LONGSPOON, REACH_OF_THE_VOID, INCOMPLETE_CONSTRUCT, TRICK_ROOM, MARK_OF_DISSONANCE, MARK_OF_THE_ACCURSED, MARK_OF_AQUEOUSNESS, BONESAW, ASTRAL_TEAR);
}
