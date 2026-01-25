package net.capozi.menagerie.mixin.access;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker("isOnSoulSpeedBlock")
    boolean invokeIsOnSoulSpeedBlock();
    @Invoker("displaySoulSpeedEffects")
    void invokeDisplaySoulSpeedEffects();
    @Accessor("SOUL_SPEED_BOOST_ID")
    UUID SOUL_SPEED_BOOST_ID();
}
