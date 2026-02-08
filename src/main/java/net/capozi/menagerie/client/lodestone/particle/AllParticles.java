package net.capozi.menagerie.client.lodestone.particle;

import net.capozi.menagerie.foundation.ParticleInit;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

import java.awt.*;

public class AllParticles {
    public static void glowAura(World world, Vec3d pos) {
        Color colour = new Color(0, 255, 244);
        WorldParticleBuilder.create(LodestoneParticleRegistry.SPARK_PARTICLE)
                .setScaleData(GenericParticleData.create(RandomHelper.randomBetween(Random.create(), 5, 12), 2).build())
                .setTransparencyData(GenericParticleData.create(0.35f, 0f).build())
                .setColorData(ColorParticleData.create(colour).build())
                .setLifetime(20)
                .addMotion(Random.create().nextBoolean() ? 0.12f : -0.12f, 0, Random.create().nextBoolean() ? 0.12f : -0.12f)
                .setSpinData(SpinParticleData.create(RandomHelper.randomBetween(Random.create(), -0.2f, 0.2f)).build())
                .enableNoClip()
                .disableCull()
                .setShouldCull(false)
                .spawn(world, pos.x, pos.y, pos.z);
    }
    public static void circleParticle(World world, Vec3d pos) {
        Color startColour = new Color(0, 255, 244);
        Color endingColor = new Color(0, 234, 190);
        WorldParticleBuilder.create(ParticleInit.CIRCLE)
                .setScaleData(GenericParticleData.create(1f, 5f).setEasing(Easing.ELASTIC_IN_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.95f, 0).build())
                .setColorData(ColorParticleData.create(startColour, endingColor).setCoefficient(1.4f).build())
                .setLifetime(120)
                .enableNoClip()
                .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 90, 0)))
                .disableCull()
                .spawn(world, pos.x, pos.y + 0.51, pos.z);
    }
    public static void circleLongParticle(World world, Vec3d pos) {
        Color startColour = new Color(0, 255, 244);
        Color endingColor = new Color(0, 234, 190);
        WorldParticleBuilder.create(ParticleInit.CIRCLE)
                .setScaleData(GenericParticleData.create(5f, 6f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.95f, 0.95f, 0).build())
                .setColorData(ColorParticleData.create(startColour, endingColor).setCoefficient(1.4f).build())
                .setLifetime(200)
                .enableNoClip()
                .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 90, 0)))
                .disableCull()
                .setLifeDelay(90)
                .spawn(world, pos.x, pos.y + 0.51, pos.z);
    }
    public static void shockwaveParticles(World world, Vec3d pos) {
        Color startColour = new Color(0, 255, 244);
        Color endingColor = new Color(0, 255, 244);
        WorldParticleBuilder.create(ParticleInit.SHOCKWAVE)
                .setScaleData(GenericParticleData.create(1f, 70f).build())
                .setTransparencyData(GenericParticleData.create(0.95f, 0).build())
                .setColorData(ColorParticleData.create(startColour, endingColor).setCoefficient(1.4f).build())
                .setSpinData(SpinParticleData.create(0f, 0f).build())
                .setLifetime(155)
                .setLifeDelay(240)
                .enableNoClip()
                .disableCull()
                .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 90, 0)))
                .spawn(world, pos.x, pos.y, pos.z);
    }
}
