package net.capozi.menagerie.common.item;

import net.capozi.menagerie.client.lodestone.particle.AllParticles;
import net.capozi.menagerie.client.render.FlashOverlayRenderer;
import net.capozi.menagerie.common.entity.HealthUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (target instanceof ServerPlayerEntity targetPlayer && target instanceof TrappedState trappedState) {
            FlashOverlayRenderer.triggerFlash();
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
       // AllParticles.circleLongParticleSKY(world, Vec3d.ofCenter(new Vec3i(user.getBlockPos().getX(), user.getBlockPos().getY() + 50, user.getBlockPos().getZ())));
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
