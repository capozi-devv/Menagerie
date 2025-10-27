package net.capozi.menagerie.common.entity.object;

import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class ChainsEntity extends AmbientEntity implements ChainsEntityOverrides {
    public final AnimationState IdleAnimationState = new AnimationState();
    private static final TrackedData<Optional<UUID>> PLAYER_UUID = DataTracker.registerData(ChainsEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private int IdleAnimationTimeout = 0;
    public ChainsEntity(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
        this.setVelocity(Vec3d.ZERO);
    }
    public static DefaultAttributeContainer.Builder createChainAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2f)
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
        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld serverWorld) {
            ServerPlayerEntity player = (ServerPlayerEntity) serverWorld.getPlayerByUuid(getPlayerUuid());
            if (player != null) {
                player.removeStatusEffect(EffectInit.CHAINED_EFFECT);
            }
        }
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
        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld serverWorld) {
            ServerPlayerEntity player = (ServerPlayerEntity) serverWorld.getPlayerByUuid(getPlayerUuid());
            if (player != null) {
                this.teleport(player.getX(), player.getY(), player.getZ());
            }
        }
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
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PLAYER_UUID, Optional.empty());
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getPlayerUuid() != null) {
            nbt.putUuid("Owner", this.getPlayerUuid());
        }
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        UUID uUID;
        if (nbt.containsUuid("Owner")) {
            uUID = nbt.getUuid("Owner");
        } else {
            String string = nbt.getString("Owner");
            uUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }
        if (uUID != null) {
            try {
                this.setPlayerUuid(uUID);
            } catch (Throwable ignored) {

            }
        }
    }
    @Nullable
    public UUID getPlayerUuid() {
        return (UUID)this.dataTracker.get(PLAYER_UUID).orElse(null);
    }
    private boolean hasCameraItem(ServerPlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(ItemInit.CAMERA_OF_THE_OTHERSIDE)) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void applyDamage(DamageSource source, float amount) {
        if(source.getAttacker() instanceof ServerPlayerEntity) {
            if(hasCameraItem((ServerPlayerEntity)source.getAttacker()) && ((ServerPlayerEntity)source.getAttacker()).getMainHandStack().isEmpty()) {
                super.applyDamage(source, amount);
            } else {
                super.applyDamage(source, 0f);
            }
        }
    }
    public void setPlayerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(PLAYER_UUID, Optional.ofNullable(uuid));
    }
}
