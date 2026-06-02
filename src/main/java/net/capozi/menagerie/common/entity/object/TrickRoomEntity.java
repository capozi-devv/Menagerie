package net.capozi.menagerie.common.entity.object;

import net.capozi.menagerie.common.entity.TrickRoomCollision;
import net.capozi.menagerie.foundation.EntityInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TrickRoomEntity extends Entity {
    public static final int FADE_OUT_TICKS = 60;

    private static final String MIN_X_KEY = "MinX";
    private static final String MIN_Y_KEY = "MinY";
    private static final String MIN_Z_KEY = "MinZ";
    private static final String MAX_X_KEY = "MaxX";
    private static final String MAX_Y_KEY = "MaxY";
    private static final String MAX_Z_KEY = "MaxZ";
    private static final String FADE_TICKS_KEY = "FadeTicks";

    private static final TrackedData<Integer> MIN_X = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MIN_Y = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MIN_Z = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_X = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_Y = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_Z = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> FADE_TICKS = DataTracker.registerData(TrickRoomEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public TrickRoomEntity(EntityType<? extends TrickRoomEntity> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public static TrickRoomEntity create(World world, BlockPos footBlock) {
        TrickRoomEntity room = new TrickRoomEntity(EntityInit.TRICK_ROOM, world);
        room.setRoomBounds(TrickRoomCollision.boundsCenteredAtFeet(footBlock));
        return room;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(MIN_X, 0);
        this.dataTracker.startTracking(MIN_Y, 0);
        this.dataTracker.startTracking(MIN_Z, 0);
        this.dataTracker.startTracking(MAX_X, 0);
        this.dataTracker.startTracking(MAX_Y, 0);
        this.dataTracker.startTracking(MAX_Z, 0);
        this.dataTracker.startTracking(FADE_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        this.setVelocity(Vec3d.ZERO);
        tickFadeOut();
        if (hasRoomBounds()) {
            refreshRoomBoundingBox();
        }
    }

    public void setRoomBounds(Box bounds) {
        setRoomBounds(
                MathHelper.floor(bounds.minX),
                MathHelper.floor(bounds.minY),
                MathHelper.floor(bounds.minZ),
                MathHelper.ceil(bounds.maxX),
                MathHelper.ceil(bounds.maxY),
                MathHelper.ceil(bounds.maxZ)
        );
    }

    public Box getRoomBounds() {
        return new Box(
                this.dataTracker.get(MIN_X),
                this.dataTracker.get(MIN_Y),
                this.dataTracker.get(MIN_Z),
                this.dataTracker.get(MAX_X),
                this.dataTracker.get(MAX_Y),
                this.dataTracker.get(MAX_Z)
        );
    }

    public boolean hasRoomBounds() {
        return this.dataTracker.get(MAX_X) > this.dataTracker.get(MIN_X)
                && this.dataTracker.get(MAX_Y) > this.dataTracker.get(MIN_Y)
                && this.dataTracker.get(MAX_Z) > this.dataTracker.get(MIN_Z);
    }

    public void beginFadeOut() {
        if (!isFadingOut()) {
            this.dataTracker.set(FADE_TICKS, FADE_OUT_TICKS);
        }
    }

    public boolean isFadingOut() {
        return this.dataTracker.get(FADE_TICKS) > 0;
    }

    public float getFadeAlpha(float tickDelta) {
        int fadeTicks = this.dataTracker.get(FADE_TICKS);
        if (fadeTicks <= 0) {
            return 1.0F;
        }
        return MathHelper.clamp((fadeTicks - tickDelta) / (float) FADE_OUT_TICKS, 0.0F, 1.0F);
    }

    @Override
    protected Box calculateBoundingBox() {
        return hasRoomBounds() ? getRoomBounds() : super.calculateBoundingBox();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if ((data == MIN_X || data == MIN_Y || data == MIN_Z || data == MAX_X || data == MAX_Y || data == MAX_Z)
                && hasRoomBounds()) {
            refreshRoomBoundingBox();
        }
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return hasRoomBounds() ? getRoomBounds() : super.getVisibilityBoundingBox();
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains(MIN_X_KEY)
                && nbt.contains(MIN_Y_KEY)
                && nbt.contains(MIN_Z_KEY)
                && nbt.contains(MAX_X_KEY)
                && nbt.contains(MAX_Y_KEY)
                && nbt.contains(MAX_Z_KEY)) {
            setRoomBounds(
                    nbt.getInt(MIN_X_KEY),
                    nbt.getInt(MIN_Y_KEY),
                    nbt.getInt(MIN_Z_KEY),
                    nbt.getInt(MAX_X_KEY),
                    nbt.getInt(MAX_Y_KEY),
                    nbt.getInt(MAX_Z_KEY)
            );
        }
        if (nbt.contains(FADE_TICKS_KEY)) {
            this.dataTracker.set(FADE_TICKS, nbt.getInt(FADE_TICKS_KEY));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt(MIN_X_KEY, this.dataTracker.get(MIN_X));
        nbt.putInt(MIN_Y_KEY, this.dataTracker.get(MIN_Y));
        nbt.putInt(MIN_Z_KEY, this.dataTracker.get(MIN_Z));
        nbt.putInt(MAX_X_KEY, this.dataTracker.get(MAX_X));
        nbt.putInt(MAX_Y_KEY, this.dataTracker.get(MAX_Y));
        nbt.putInt(MAX_Z_KEY, this.dataTracker.get(MAX_Z));
        nbt.putInt(FADE_TICKS_KEY, this.dataTracker.get(FADE_TICKS));
    }

    private void setRoomBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.dataTracker.set(MIN_X, minX);
        this.dataTracker.set(MIN_Y, minY);
        this.dataTracker.set(MIN_Z, minZ);
        this.dataTracker.set(MAX_X, maxX);
        this.dataTracker.set(MAX_Y, maxY);
        this.dataTracker.set(MAX_Z, maxZ);
        this.setPos((minX + maxX) * 0.5D, (minY + maxY) * 0.5D, (minZ + maxZ) * 0.5D);
        refreshRoomBoundingBox();
    }

    private void refreshRoomBoundingBox() {
        this.setBoundingBox(getRoomBounds());
    }

    private void tickFadeOut() {
        int fadeTicks = this.dataTracker.get(FADE_TICKS);
        if (fadeTicks <= 0 || this.getWorld().isClient()) {
            return;
        }
        if (fadeTicks <= 1) {
            this.discard();
        } else {
            this.dataTracker.set(FADE_TICKS, fadeTicks - 1);
        }
    }
}
