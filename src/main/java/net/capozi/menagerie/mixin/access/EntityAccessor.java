package net.capozi.menagerie.mixin.access;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("getLandingBlockState")
    BlockState invokeGetLandingBlockState();
}
