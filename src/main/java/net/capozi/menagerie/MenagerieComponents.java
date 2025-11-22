package net.capozi.menagerie;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.capozi.menagerie.common.network.*;
import net.minecraft.util.Identifier;

public class MenagerieComponents implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        Menagerie.BOUND_ARTIFACT = ComponentRegistry.getOrCreate(new Identifier(Menagerie.MOD_ID, "bound_artifact"), BoundArtifactComponent.class);
        Menagerie.BOUND_ACCURSED = ComponentRegistry.getOrCreate(new Identifier(Menagerie.MOD_ID, "bound_accursed"), BoundAccursedComponent.class);
        Menagerie.BOUND_AQUEOUS = ComponentRegistry.getOrCreate(new Identifier(Menagerie.MOD_ID, "bound_aqueous"), BoundAqueousComponent.class);
        registry.registerForPlayers(Menagerie.BOUND_AQUEOUS, player -> (BoundAqueousComponent) new BoundAqueousComponentImpl(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(Menagerie.BOUND_ARTIFACT, player -> (BoundArtifactComponent) new BoundArtifactComponentImpl(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(Menagerie.BOUND_ACCURSED, player -> (BoundAccursedComponent) new BoundAccursedComponentImpl(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}