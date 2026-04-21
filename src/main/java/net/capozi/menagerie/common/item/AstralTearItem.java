package net.capozi.menagerie.common.item;

import net.capozi.menagerie.client.lodestone.particle.AllParticles;
import net.capozi.menagerie.common.entity.object.CircleBeamEntity;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

import static net.capozi.menagerie.foundation.EntityInit.CIRCLE;

public class AstralTearItem extends Item {
    public AstralTearItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        CircleBeamEntity circleBeam = new CircleBeamEntity(CIRCLE, context.getWorld());
        context.getWorld().spawnEntity(circleBeam);
        circleBeam.setPos(context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ());
        return ActionResult.PASS;
    }
}
