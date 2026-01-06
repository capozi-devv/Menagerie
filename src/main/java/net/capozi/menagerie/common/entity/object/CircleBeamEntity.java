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
            CircleBeamRenderer.shouldRender = false;
            ticksAlive = 0;
            triggered = false;
            this.discard();
        } else if (this.age >= 80){
            CircleBeamRenderer.shouldRender = true;
            DamageSource source = new DamageSource(this.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.PLAYER_EXPLOSION));
            if (this.age % 2 == 0) {
                this.getWorld().createExplosion(null, null, null, this.getPos().add(RandomHelper.randomBetween(Random.create(), -9f, 9f), RandomHelper.randomBetween(Random.create(), -9f, 9f), RandomHelper.randomBetween(Random.create(), -6f, 6f)), 8, true, World.ExplosionSourceType.MOB);
                Vec3d center = this.getPos();
                double radius = 20.0;
                Box box = new Box(
                        center.x - radius, center.y - (radius/2), center.z - radius,
                        center.x + radius, center.y + (radius/2), center.z + radius
                );
                List<LivingEntity> entities = this.getWorld().getEntitiesByClass(
                        LivingEntity.class,
                        box,
                        e -> e.squaredDistanceTo(center) <= radius * radius
                );
                for (LivingEntity entity : entities) {
                    double x = this.getX() - entity.getX();
                    double z = this.getZ() - entity.getZ();
                    if ((Entity)entity == this) return;
                    entity.damage(source, 6);
                    entity.takeKnockback(7, x, z);
                }
            }
            if (!FlashOverlayRenderer.isFlashing() && !triggered) {
                triggered = true;
                FlashOverlayRenderer.triggerFlash();
                //FlashPacket.sendToTracking((ServerWorld)this.getWorld(), this);
            }
        } else {
            CircleBeamRenderer.shouldRender = false;
        }
        super.tick();
    }

    // ! pass over to default entity logic
    @Override protected void initDataTracker() {}
    @Override protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override protected void writeCustomDataToNbt(NbtCompound nbt) {}
}
