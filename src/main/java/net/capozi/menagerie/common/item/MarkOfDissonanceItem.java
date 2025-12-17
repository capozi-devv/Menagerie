package net.capozi.menagerie.common.item;

import net.minecraft.item.Item;

public class MarkOfDissonanceItem extends Item {
    public MarkOfDissonanceItem(Item.Settings settings) {
        super(settings);
    }
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
//        if (!world.isClient) {
//            BoundArtifactComponent artifact = Menagerie.getBoundArtifact().get(player);
//            BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
//            BoundAqueousComponent aqueous = Menagerie.getBoundAqueous().get(player);
//            if (artifact.hasArtifact() || accursed.hasAccursed() || aqueous.hasAqueous()) {
//                player.sendMessage(Text.literal("Thy soul cannot split in twain."), true);
//                return TypedActionResult.fail(player.getStackInHand(hand));
//            }
//            artifact.setHasArtifact(true);
//            player.getItemCooldownManager().set(this, 20);
//            if (!player.getAbilities().creativeMode) {
//                player.getStackInHand(hand).decrement(1);
//            }
//            return TypedActionResult.success(player.getStackInHand(hand), false);
//        }
//        return TypedActionResult.pass(player.getStackInHand(hand));
//    }
}
