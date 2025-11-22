package net.capozi.menagerie.common.effect;

import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.common.item.TrappedState;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ChainedEffect extends StatusEffect {
    public ChainedEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x5A5A5A);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if ((entity instanceof PlayerEntity player)) {
            if (amplifier >= 0) {
                player.setVelocity(Vec3d.ZERO);
                player.velocityModified = true;
                if (!player.getWorld().isClient) {
                    player.setSneaking(false);
                }
            }
        }
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        List<ChainsEntity> chainsNearby = entity.getWorld().getEntitiesByClass(ChainsEntity.class, entity.getBoundingBox().expand(10.0), chains -> chains.isAlive());
        for (ChainsEntity chains : chainsNearby) {
            chains.discard();
        }
        if ((PlayerEntity)entity instanceof TrappedState state) {
            state.setTrapped(false);
        }
    }
}
