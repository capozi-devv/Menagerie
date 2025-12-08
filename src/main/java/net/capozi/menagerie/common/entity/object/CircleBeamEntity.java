package net.capozi.menagerie.common.entity.object;

import net.capozi.menagerie.common.entity.client.CircleBeamRenderer;
import net.capozi.menagerie.foundation.EntityInit;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;

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
    @Override
    public void tick() {
        float progress = (System.currentTimeMillis() - flashStartTime) / (float) FLASH_DURATION_MS;
        ticksAlive++;
        if (ticksAlive >= 260) {
            CircleBeamRenderer.shouldRender = false;
            ticksAlive = 0;
            this.discard();
        } else if (ticksAlive >= 80){
            CircleBeamRenderer.shouldRender = true;
            DamageSource source = new DamageSource(this.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.PLAYER_EXPLOSION));
            ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(70, this.getPos(), 50, 50).setIntensity(7f, 4f, 0.5f).setEasing(Easing.QUAD_OUT));
            Vec3d center = this.getPos();
            double radius = 8.0;
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
