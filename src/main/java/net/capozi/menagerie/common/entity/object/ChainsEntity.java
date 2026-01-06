package net.capozi.menagerie.common.entity.object;

import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChainsEntity extends AmbientEntity {
    private int i;
    public PlayerEntity chainedPlayer;
    private static final TrackedData<Boolean> DEATH_ANIMATION = DataTracker.registerData(ChainsEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SPAWN_BEAM = DataTracker.registerData(ChainsEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> STARTUP_TICKS = DataTracker.registerData(ChainsEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public ChainsEntity(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createChainAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1)
                .add(EntityAttributes.GENERIC_ARMOR, 0f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 666666);
    }

    private void SetupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(1000)+2000;
            this.idleAnimationState.start(this.age);
        } else {
            this.idleAnimationTimeout--;
        }
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DEATH_ANIMATION, false);
        this.dataTracker.startTracking(SPAWN_BEAM, false);
        this.dataTracker.startTracking(STARTUP_TICKS, 240);
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
    @Override
    public boolean canTakeDamage() {
        return true;
    }
    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            return hasCameraItem((ServerPlayerEntity)player);
        } else {
           return false;
        }
    }
    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld serverWorld) {
            List<PlayerEntity> players = this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(10.0), entity -> entity.isAlive());
            for (PlayerEntity player : players){
                if (player != null) {
                    player.removeStatusEffect(EffectInit.CHAINED_EFFECT);
                }
            }
        }
        setDespawnCounter(despawnCounter=0);
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
    public void tick() {
        World world = this.getWorld();
        Vec3d pos =  this.getPos();
        BlockPos blockPos = this.getBlockPos();
        SetupAnimationStates();
        if (this.isDead()) {
            this.remove(RemovalReason.DISCARDED);
        }
        i = 0;
        if (!this.getPassengerList().isEmpty()) {
            if (this.getPassengerList().get(0) instanceof PlayerEntity rider) {
                rider.setHealth(rider.getMaxHealth());
                rider.setSneaking(false);
                if (chainedPlayer == null) {
                    chainedPlayer = rider;
                } else {
                    chainedPlayer.startRiding(this, true);
                    chainedPlayer.updatePosition(pos.x, pos.y, pos.z);
                }
            }
        }
    }
    public boolean getDeathAnimation() {
        return dataTracker.get(DEATH_ANIMATION);
    }

    public void setDeathAnimation(Boolean bool) {
        dataTracker.set(DEATH_ANIMATION, bool);
    }

    public void setShouldSpawnBeam(Boolean bool) {
        dataTracker.set(SPAWN_BEAM, bool);
    }
    public boolean ShouldSpawnBeam() {
        return dataTracker.get(SPAWN_BEAM);
    }


    public int getStartupTicks() {
        return dataTracker.get(STARTUP_TICKS);
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        positionUpdater.accept(passenger, this.getX(), this.getY(), this.getZ());
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        super.setNoGravity(true);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return null;
    }
}
