package net.capozi.menagerie.common.item;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.entity.HealthUtils;
import net.capozi.menagerie.server.cca.BoundAccursedComponent;
import net.capozi.menagerie.server.cca.BoundAqueousComponent;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MarkRemoverItem extends Item {
    public MarkRemoverItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient()) {
            BoundArtifactComponent artifact = Menagerie.getBoundArtifact().get(player);
            BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
            BoundAqueousComponent aqueous = Menagerie.getBoundAqueous().get(player);
            if (artifact.hasArtifact()) {
                artifact.setHasArtifact(false);
            } else if (accursed.hasAccursed()) {
                accursed.setHasAccursed(false);
                HealthUtils.removeExtraHearts(player);
            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}
