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
        AllParticles.executeGlowAura(context.getWorld(), Vec3d.ofCenter(context.getBlockPos()));
        AllParticles.circleParticle(context.getWorld(), Vec3d.ofCenter(context.getBlockPos()));
        AllParticles.circleLongParticle(context.getWorld(), Vec3d.ofCenter(context.getBlockPos()));
        context.getWorld().playSound(null, context.getBlockPos(), SoundInit.REVIVAL, SoundCategory.PLAYERS, 1f, 1f);
        return ActionResult.PASS;
    }
}
