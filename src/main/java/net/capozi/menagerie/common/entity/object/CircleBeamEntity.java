package net.capozi.menagerie.common.entity.object;

import net.capozi.menagerie.client.lodestone.particle.AllParticles;
import net.capozi.menagerie.client.render.FlashOverlayRenderer;
import net.capozi.menagerie.common.entity.client.CircleBeamRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import team.lodestar.lodestone.helpers.RandomHelper;

import java.util.List;

import static net.capozi.menagerie.common.entity.client.CircleBeamRenderer.FLASH_DURATION_MS;
import static net.capozi.menagerie.common.entity.client.CircleBeamRenderer.flashStartTime;

public class CircleBeamEntity extends Entity {
    public CircleBeamEntity(EntityType<? extends CircleBeamEntity> type, World world) {
        super(type, world);
    }
    public void setBeamPos(CircleBeamEntity entity, int x, int y, int z) {
        entity.setPos(x, y, z);
    }
    private int ticksAlive = 0;
    public static boolean triggered = false;
    @Override
    public void tick() {
        float progress = (System.currentTimeMillis() - flashStartTime) / (float) FLASH_DURATION_MS;
        ticksAlive++;
        if (this.age >= 260) {
            CircleBeamRenderer.setShouldRender(false);
            ticksAlive = 0;
            triggered = false;
            this.discard();
        } else if (this.age >= 80){
            CircleBeamRenderer.setShouldRender(true);
            if (this.age % 2 == 0) {
                this.getWorld().createExplosion(null, null, null, this.getPos().add(RandomHelper.randomBetween(Random.create(), -9f, 9f), RandomHelper.randomBetween(Random.create(), -9f, 9f), RandomHelper.randomBetween(Random.create(), -6f, 6f)), 8, true, World.ExplosionSourceType.MOB);
            }
            if (!FlashOverlayRenderer.isFlashing() && !triggered) {
                triggered = true;
                FlashOverlayRenderer.triggerFlash();
                //FlashPacket.sendToTracking((ServerWorld)this.getWorld(), this);
            }
        } else {
            CircleBeamRenderer.setShouldRender(false);
        }
        super.tick();
    }
    @Override protected void initDataTracker() {}
    @Override protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override protected void writeCustomDataToNbt(NbtCompound nbt) {}
}
