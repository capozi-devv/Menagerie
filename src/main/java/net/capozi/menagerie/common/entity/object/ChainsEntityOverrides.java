package net.capozi.menagerie.common.entity.object;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;

public interface ChainsEntityOverrides {
    boolean canBeRiddenInWater();
    boolean canStartRiding(Entity entity, boolean force);
    boolean canBeRiddenBy(Entity entity);
    EntityPose getMountPose();
    Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor);
}
