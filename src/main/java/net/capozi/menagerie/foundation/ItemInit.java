package net.capozi.menagerie.foundation;

import devv.capozi.zip.common.api.index.Registrar;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.item.*;
import net.capozi.menagerie.common.item.material.ReachOftheVoidMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface ItemInit {
    static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemInit::registerItemToToolsTabItemGroup);
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemInit::registerBlocksToBuildingTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ItemInit::registerItemsToWeaponGroup);
        itemRegistrar.setRegistries(itemRegistrar.entries, itemRegistrar.registry_consumer);
    }
    static void registerItemsToWeaponGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.TRIDENT, HEAVYIRON_LONGSPOON);
        entries.addAfter(HEAVYIRON_LONGSPOON, BONESAW);
        entries.addAfter(BONESAW, PUNCH_UP_STAR);
    }
    static void registerItemToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MUSIC_DISC_RELIC, DAMNATIO_MEMORIAE_MUSIC_DISC);
        entries.addAfter(DAMNATIO_MEMORIAE_MUSIC_DISC, CAMERA_OF_THE_OTHERSIDE);
        entries.addAfter(CAMERA_OF_THE_OTHERSIDE, SIMULACRUM_VESSEL);
        entries.addAfter(SIMULACRUM_VESSEL, REACH_OF_THE_VOID);
        entries.addAfter(REACH_OF_THE_VOID, ASTRAL_TEAR);
        entries.addAfter(ASTRAL_TEAR, DECRYPTORS_EYE);
        entries.addAfter(DECRYPTORS_EYE, MARK_OF_DISSONANCE);
        entries.addAfter(MARK_OF_DISSONANCE, MARK_OF_THE_ACCURSED);
        entries.addAfter(MARK_OF_THE_ACCURSED, MARK_OF_AQUEOUSNESS);
    }
//    public static void registerBlocksToBuildingTab(FabricItemGroupEntries entries) {
//        entries.add(CAPOZI_PLUSH);
//        entries.add(EYA_PLUSH);
//        entries.add(COSMO_PLUSH);
//    }
    Registrar<Item> itemRegistrar = new Registrar<Item>(((id, item) -> Registry.register(Registries.ITEM, id, item)));
    Item CAMERA_OF_THE_OTHERSIDE = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "camera_of_the_otherside"), new CameraOfTheOthersideItem(new FabricItemSettings().maxCount(1)));
    Item SIMULACRUM_VESSEL = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "simulacrum_vessel"), new SimulacrumVesselItem(new FabricItemSettings().maxCount(16)));
    Item TEST_ITEM = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "test_item"), new TestItem(new FabricItemSettings()));
    Item DAMNATIO_MEMORIAE_MUSIC_DISC = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "damnatio_memoriae"), new MusicDiscItem(7, SoundInit.DAMNATIO_MEMORIAE, new FabricItemSettings().maxCount(1), 83));
    Item HEAVYIRON_LONGSPOON = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "heavyiron_longspoon"), new HeavyIronLongSpoonItem(ToolMaterials.NETHERITE, 6, -3.2F, new Item.Settings().maxCount(1).fireproof()));
    Item REACH_OF_THE_VOID = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "reach_of_the_void"), new ReachOfTheVoidItem(new ReachOftheVoidMaterial(), new Item.Settings().maxCount(1)));
    Item INCOMPLETE_CONSTRUCT = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "incomplete_construct"), new Item(new Item.Settings().maxCount(1)));
    Item TRICK_ROOM = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "trick_room"), new TrickRoomItem(new Item.Settings().maxCount(1)));
    Item MARK_OF_DISSONANCE = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_of_dissonance"), new MarkOfDissonanceItem(new Item.Settings().maxCount(1)));
    Item MARK_OF_THE_ACCURSED = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_of_the_accursed"), new MarkOfTheAccursedItem(new Item.Settings().maxCount(1)));
    Item MARK_OF_AQUEOUSNESS = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_of_aqueousness"), new MarkOfAqueousnessItem(new Item.Settings().maxCount(1)));
    Item ASTRAL_TEAR = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "astral_tear"), new AstralTearItem(new Item.Settings().maxCount(1)));
    Item BONESAW = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "bonesaw"), new BonesawItem(ToolMaterials.NETHERITE, 4, -2.8f, new  Item.Settings().maxCount(1)));
    Item MARK_REMOVER = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "mark_remover"), new MarkRemoverItem(new Item.Settings()));
    Item DECRYPTORS_EYE = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "decryptors_eye"), new DecryptorsEyeTrinketItem(new Item.Settings().maxCount(1)));
    Item PUNCH_UP_STAR = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "punch_up_star"), new PunchUpStarItem(ToolMaterials.NETHERITE, new Item.Settings().fireproof().maxCount(1)));
    Item FLYWHEEL_FRAMEWORK = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "flywheel_framework"), new Item(new Item.Settings().maxCount(32).fireproof()));
    Item KINEMATIC_CORE = itemRegistrar.add(Identifier.of(Menagerie.MOD_ID, "kinematic_core"), new Item(new Item.Settings().maxCount(1)));
}
