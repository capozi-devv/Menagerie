package net.capozi.menagerie.mixin;

import net.capozi.menagerie.common.entity.TrickRoomCollision;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class TrickRoomProjectileCollisionMixin {
    private static final double ENDER_PEARL_BORDER_REMOVAL_DISTANCE = 1.0D;

    @Shadow
    protected abstract void onCollision(HitResult hitResult);

    @Inject(method = "tick", at = @At("TAIL"))
    private void menagerie$stopProjectileAtTrickRoom(CallbackInfo ci) {
        ProjectileEntity projectile = (ProjectileEntity) (Object) this;
        if (projectile.getWorld().isClient || projectile.isRemoved()) {
            return;
        }

        Vec3d start = new Vec3d(projectile.prevX, projectile.prevY, projectile.prevZ);
        Vec3d end = projectile.getPos();
        if (projectile instanceof EnderPearlEntity && TrickRoomCollision.segmentIntersectsBarrierShell(projectile.getWorld(), start, end, ENDER_PEARL_BORDER_REMOVAL_DISTANCE)) {
            projectile.discard();
            return;
        }

        if (start.squaredDistanceTo(end) < 1.0E-8D) {
            return;
        }

        BlockHitResult roomHit = TrickRoomCollision.raycastBarrier(projectile.getWorld(), start, end);
        if (roomHit == null || roomHit.getType() == HitResult.Type.MISS) {
            return;
        }

        projectile.setPosition(roomHit.getPos());
        projectile.setVelocity(Vec3d.ZERO);
        onCollision(roomHit);
    }
}
