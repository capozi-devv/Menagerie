package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import static net.capozi.menagerie.foundation.BlockInit.*;

public class ItemInit {
    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemInit::registerItemToToolsTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ItemInit::registerBlocksToBuildingTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ItemInit::registerItemsToWeaponGroup);
    }
    private static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Menagerie.MOD_ID, name), item);
    }
    public static void registerItemsToWeaponGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.TRIDENT, HEAVYIRON_LONGSPOON);
    }
    public static void registerItemToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MUSIC_DISC_RELIC, DAMNATIO_MEMORIAE_MUSIC_DISC);
        entries.addAfter(DAMNATIO_MEMORIAE_MUSIC_DISC, CAMERA_OF_THE_OTHERSIDE);
        entries.addAfter(CAMERA_OF_THE_OTHERSIDE, SIMULACRUM_VESSEL);
        entries.addAfter(SIMULACRUM_VESSEL, REACH_OF_THE_VOID);
        entries.addAfter(REACH_OF_THE_VOID, TRICK_ROOM);
    }
    public static void registerBlocksToBuildingTab(FabricItemGroupEntries entries) {
        entries.add(CAPOZI_PLUSH);
        entries.add(EYA_PLUSH);
        entries.add(COSMO_PLUSH);
    }
    public static final Item CAMERA_OF_THE_OTHERSIDE = registerItems("camera_of_the_otherside",new CameraOfTheOthersideItem(new FabricItemSettings().maxCount(1)));
    public static final Item SIMULACRUM_VESSEL = registerItems("simulacrum_vessel", new SimulacrumVesselItem(new FabricItemSettings().maxCount(16)));
    public static final Item TEST_ITEM = registerItems("test_item", new TestItem(new FabricItemSettings()));
    public static final Item DAMNATIO_MEMORIAE_MUSIC_DISC = registerItems("damnatio_memoriae", new MusicDiscItem(7, SoundInit.DAMNATIO_MEMORIAE, new FabricItemSettings().maxCount(1), 83));
    public static final Item HEAVYIRON_LONGSPOON = registerItems("heavyiron_longspoon", new HeavyIronLongSpoonItem(ToolMaterials.NETHERITE, 6, -3.2F, new Item.Settings().maxCount(1).fireproof()));
    public static final Item REACH_OF_THE_VOID = registerItems("reach_of_the_void", new ReachOfTheVoidItem(new Item.Settings().maxCount(1)));
    public static final Item INCOMPLETE_CONSTRUCT = registerItems("incomplete_construct", new Item(new Item.Settings()));
    public static final Item TRICK_ROOM = registerItems("trick_room", new AstralTearItem(new Item.Settings().maxCount(1)));
    public static final Item MARK_OF_DISSONANCE = registerItems("mark_of_dissonance", new MarkOfDissonanceItem(new Item.Settings().maxCount(1)));
    public static final Item MARK_OF_THE_ACCURSED = registerItems("mark_of_the_accursed", new MarkOfDissonanceItem(new Item.Settings().maxCount(1)));
    public static final Item MARK_OF_AQUEOUSNESS = registerItems("mark_of_aqueousness", new MarkOfDissonanceItem(new Item.Settings().maxCount(1)));
    public static final Item ASTRAL_TEAR = registerItems("astral_tear", new AstralTearItem(new Item.Settings().maxCount(1)));
    public static final Item BONESAW = registerItems("bonesaw", new BonesawItem(ToolMaterials.NETHERITE, 5, -3f, new  Item.Settings().maxCount(1)));
}
