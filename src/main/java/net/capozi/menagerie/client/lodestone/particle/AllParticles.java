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
    public static void glowAura(float scale, World world, Vec3d pos, float opacity) {
        Color colour = new Color(0, 255, 244);
        WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setScaleData(GenericParticleData.create(scale).setEasing(Easing.ELASTIC_OUT).build())
                .setTransparencyData(GenericParticleData.create(opacity, opacity, 0).build())
                .setColorData(ColorParticleData.create(colour).setCoefficient(1.4f).build())
                .setLifetime(250)
                .enableNoClip()
                .setLifeDelay(80)
                .spawn(world, pos.x, pos.y, pos.z)
                .repeat(world, pos.x, pos.y, pos.z, 2);
    }
    public static void executeGlowAura(World world, Vec3d pos) {
        glowAura(3f, world, pos, 0.9f);
        glowAura(5f, world, pos, 0.75f);
        glowAura(7f, world, pos, 0.6f);
        glowAura(9f, world, pos, 0.25f);
    }
    public static void circleParticle(World world, Vec3d pos) {
        Color startColour = new Color(0, 255, 244);
        Color endingColor = new Color(0, 234, 190);
        WorldParticleBuilder.create(ParticleInit.CIRCLE)
                .setScaleData(GenericParticleData.create(1f, 5f).setEasing(Easing.ELASTIC_IN_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.95f, 0.95f, 0).build())
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
                .setScaleData(GenericParticleData.create(5f).setEasing(Easing.ELASTIC_IN_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.95f, 0.95f, 0).build())
                .setColorData(ColorParticleData.create(startColour, endingColor).setCoefficient(1.4f).build())
                .setLifetime(200)
                .enableNoClip()
                .setBehavior(new DirectionalBehaviorComponent(new Vec3d(0, 90, 0)))
                .disableCull()
                .setLifeDelay(90)
                .spawn(world, pos.x, pos.y + 0.51, pos.z);
    }
}
