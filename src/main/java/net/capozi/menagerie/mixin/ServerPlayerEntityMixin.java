package net.capozi.menagerie.mixin;

import com.mojang.authlib.GameProfile;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.common.item.TrappedState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements TrappedState {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    @Unique
    private ChainsEntity trappedChains;
    @Override
    public ChainsEntity getTrappedChains() {
        return this.trappedChains;
    }
    @Override
    public void setTrappedChains(ChainsEntity chains) {
        this.trappedChains = chains;
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void cancelMovementWhenFrozen(CallbackInfo ci) {
        if (this.hasStatusEffect(StatusEffects.RESISTANCE)) {
            StatusEffectInstance resistance = this.getStatusEffect(StatusEffects.RESISTANCE);
            World serverWorld = this.getWorld();
            ChainsEntity chains = new ChainsEntity(EntityInit.ABYSSAL_CHAINS, serverWorld);
            if (resistance != null && resistance.getAmplifier() == 255) {
                this.setVelocity(Vec3d.ZERO);
                chains.setVelocity(Vec3d.ZERO);
                if (!this.isTrapped()) {
                    this.setTrapped(true);
                    Menagerie.LOGGER.info("Player " + this.getName().getString() + " is now trapped.");
                    Vec3d pos = this.getPos();
                    chains.refreshPositionAndAngles(pos.x, pos.y, pos.z, 0, 0);
                    serverWorld.spawnEntity(chains);
                    this.velocityModified = true;
                    chains.velocityModified =true;
                    this.trappedChains = chains;
                }
                return;
            }
        }
    }
}
