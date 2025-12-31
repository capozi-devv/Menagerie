package net.capozi.menagerie.foundation;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.capozi.menagerie.Menagerie;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class ParticleInit {
    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(Registries.PARTICLE_TYPE, Menagerie.MOD_ID);
    public static RegistryObject<LodestoneWorldParticleType> CIRCLE = PARTICLES.register("circle", LodestoneWorldParticleType::new);
    public static RegistryObject<LodestoneWorldParticleType> SHOCKWAVE = PARTICLES.register("shockwave", LodestoneWorldParticleType::new);
    public static void init() {
        ParticleFactoryRegistry.getInstance().register(CIRCLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SHOCKWAVE.get(), LodestoneWorldParticleType.Factory::new);
    }
}
