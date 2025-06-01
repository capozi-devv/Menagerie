package net.capozi.menagerie.common.entity.object;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class ChainsEntity extends AmbientEntity implements ChainsEntityOverrides {
    public final AnimationState IdleAnimationState = new AnimationState();
    private int IdleAnimationTimeout = 0;
    public ChainsEntity(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
        this.setVelocity(Vec3d.ZERO);
    }
    public static DefaultAttributeContainer.Builder createChainAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 100f);

    }
    private void SetupAnimationStates() {
        if (this.IdleAnimationTimeout <= 0) {
            this.IdleAnimationTimeout = this.random.nextInt(1000)+2000;
            this.IdleAnimationState.start(this.age);
        } else {
            this.IdleAnimationTimeout--;
        }
    }
    @Override
    protected boolean isImmobile() {
        return super.isImmobile();
    }
    @Override
    public boolean isInvulnerable() {
        return true;
    }
    @Override
    public boolean canBeRiddenInWater() {
        return true;
    }
    @Override
    public boolean doesNotCollide(double offsetX, double offsetY, double offsetZ) {
        return true;
    }
    @Override
    public boolean canStartRiding(Entity entity, boolean force) {
        return false; // Nothing can ride this
    }
    @Override
    public boolean canBeRiddenBy(Entity entity) {
        return true; // But it can be ridden
    }
    @Override
    public EntityPose getMountPose() {
        return null;
    }
    @Override
    public boolean hasPassengers() {
        return !super.getPassengerList().isEmpty();
    }
    @Override
    public void onDeath(DamageSource damageSource) {
        setDespawnCounter(despawnCounter=0);
    }
    @Override
    public Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return this.getPos().add(0, 0, 0);
    }
    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        Vec3d pos = this.getPos().add(0, 0, 0);
        positionUpdater.accept(passenger, pos.x, pos.y, pos.z);
    }
    @Override
    public boolean hasNoGravity() {
        return true;
    }
    @Override
    public void tick() {
        super.tick();
        this.setVelocity(0, 0, 0);
        this.setVelocity(Vec3d.ZERO);
        if (this.isDead()) {
            this.discard();
        }
        if (this.getWorld().isClient())
            SetupAnimationStates();
        if (!this.getPassengerList().isEmpty()) {
            Entity rider = this.getPassengerList().get(0);
            rider.setSneaking(false);
            if (rider.isLiving()) {
                LivingEntity livingRider = (LivingEntity) rider;
                livingRider.setHealth(livingRider.getMaxHealth());
            }
        }
    }
}
