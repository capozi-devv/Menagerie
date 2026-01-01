package net.capozi.menagerie.common.item;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.server.cca.BoundAccursedComponent;
import net.capozi.menagerie.server.cca.BoundAqueousComponent;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MarkOfAqueousnessItem extends Item {
    public MarkOfAqueousnessItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            BoundArtifactComponent artifact = Menagerie.getBoundArtifact().get(user);
            BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(user);
            BoundAqueousComponent aqueous = Menagerie.getBoundAqueous().get(user);
            if (artifact.hasArtifact() || accursed.hasAccursed() || aqueous.hasAqueous()) {
                user.sendMessage(Text.literal("Thy soul cannot split in twain."), true);
                return TypedActionResult.fail(user.getStackInHand(hand));
            }
            aqueous.setHasAqueous(true);
            user.getItemCooldownManager().set(this, 20);
            if (!user.getAbilities().creativeMode) {
                user.getStackInHand(hand).decrement(1);
            }
            return TypedActionResult.success(user.getStackInHand(hand), false);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
