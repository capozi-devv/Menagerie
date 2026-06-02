package net.capozi.menagerie.mixin;

import net.capozi.menagerie.common.entity.TrickRoomCollision;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockView.class)
public interface TrickRoomBlockViewRaycastMixin {
    @Inject(method = "raycast(Lnet/minecraft/world/RaycastContext;)Lnet/minecraft/util/hit/BlockHitResult;", at = @At("RETURN"), cancellable = true)
    private void menagerie$makeTrickRoomRaycastsHitSurface(RaycastContext context, CallbackInfoReturnable<BlockHitResult> cir) {
        BlockHitResult roomHit = TrickRoomCollision.raycastBarrier((BlockView) this, context.getStart(), context.getEnd());
        if (roomHit == null) {
            return;
        }

        BlockHitResult vanillaHit = cir.getReturnValue();
        if (vanillaHit == null || vanillaHit.getType() == HitResult.Type.MISS || isCloser(context.getStart(), roomHit, vanillaHit)) {
            cir.setReturnValue(roomHit);
        }
    }

    private static boolean isCloser(Vec3d start, BlockHitResult candidate, BlockHitResult current) {
        return start.squaredDistanceTo(candidate.getPos()) < start.squaredDistanceTo(current.getPos());
    }
}
