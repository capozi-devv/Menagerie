package net.capozi.menagerie.common.item;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.network.BoundAccursedComponent;
import net.capozi.menagerie.common.network.BoundArtifactComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MarkOfTheAccursedItem extends Item {
    public MarkOfTheAccursedItem(Settings settings) {
        super(settings);
    }
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            BoundArtifactComponent artifact = Menagerie.getBoundArtifact().get(player);
            BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
            if (artifact.hasArtifact() || accursed.hasAccursed()) {
                player.sendMessage(Text.literal("Thy soul cannot split in twain."), true);
                return TypedActionResult.fail(player.getStackInHand(hand));
            }
            accursed.setHasAccursed(true); // This should set internal boolean true
            player.getItemCooldownManager().set(this, 20);
            if (!player.getAbilities().creativeMode) {
                player.getStackInHand(hand).decrement(1);
            }
            return TypedActionResult.success(player.getStackInHand(hand), false);
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}
