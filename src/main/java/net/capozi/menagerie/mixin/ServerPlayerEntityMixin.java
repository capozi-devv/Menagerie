package net.capozi.menagerie.mixin;

import com.mojang.authlib.GameProfile;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.network.BoundAccursedComponent;
import net.capozi.menagerie.common.network.BoundAqueousComponent;
import net.capozi.menagerie.common.network.BoundArtifactComponent;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.common.item.TrappedState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
        if (this.hasStatusEffect(EffectInit.CHAINED_EFFECT)) {
            StatusEffectInstance chained = this.getStatusEffect(EffectInit.CHAINED_EFFECT);
            World serverWorld = this.getWorld();
            ChainsEntity chains = new ChainsEntity(EntityInit.ABYSSAL_CHAINS, serverWorld);
            if (chained != null) {
                this.setVelocity(Vec3d.ZERO);
                chains.setVelocity(Vec3d.ZERO);
                if (!this.isTrapped()) {
                    this.setTrapped(true);
                    Menagerie.LOGGER.info("Player " + this.getName().getString() + " is now trapped.");
                    Vec3d pos = this.getPos();
                    chains.refreshPositionAndAngles(pos.x, pos.y, pos.z, 0, 0);
                    chains.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 12000, 1, false, false, false));
                    this.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 12000, 1, false, false, false));
                    serverWorld.spawnEntity(chains);
                    chains.setPlayerUuid(this.getUuid());
                    this.velocityModified = true;
                    chains.velocityModified =true;
                    this.trappedChains = chains;
                }
            }
        } else if (!this.hasStatusEffect(EffectInit.CHAINED_EFFECT)) {
            this.setTrapped(false);
        }
    }
    @Inject(method = "tick", at = @At("TAIL"))
    private void burnInDaylightIfAccursed(CallbackInfo ci) {
        if (this.isAlive() && !this.isSpectator() && !this.isCreative()) {
            if (Menagerie.getBoundAccursed().get(this).hasAccursed()) {
                setAir(100);
            }
            BlockPos pos = this.getBlockPos();
            if (this.getWorld().isDay() && this.getWorld().isSkyVisible(this.getBlockPos()) && !this.isSubmergedInWater() && this.getBrightnessAtEyes() > 0.5F) {
                BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(this);
                World world = this.getWorld();
                if (world.isRaining() && world.hasRain(pos)) return; // Don't burn in rain
                    ItemStack headStack = this.getEquippedStack(EquipmentSlot.HEAD);
                    boolean hasHelmet = !headStack.isEmpty();
                    if (!hasHelmet) {
                        if(this.getFireTicks() <= 0 && accursed.hasAccursed()) {
                            this.setOnFireFor(20);
                        }
                    }
            }
        }
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickCustomTimer(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
        BoundArtifactComponent artifact = Menagerie.getBoundArtifact().get(player);
        BoundAqueousComponent aqueous = Menagerie.getBoundAqueous().get(player);
        aqueous.tickAqueous(player);
        accursed.tickAccursed(player);
        artifact.tickArtifact(player);
    }
}