package net.capozi.menagerie.foundation;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.common.entity.object.CircleBeamEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityInit {
    public static final EntityType<ChainsEntity> ABYSSAL_CHAINS = Registry.register(Registries.ENTITY_TYPE, new Identifier(Menagerie.MOD_ID, "chains"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChainsEntity::new).dimensions(EntityDimensions.fixed(0.7f, 1f)).build());
    public static final EntityType<CircleBeamEntity> CIRCLE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Menagerie.MOD_ID, "circle"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CircleBeamEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.5f))
                    .fireImmune()
                    .trackedUpdateRate(10)
                    .trackRangeBlocks(64)
                    .build());
    public static void register() {}
}