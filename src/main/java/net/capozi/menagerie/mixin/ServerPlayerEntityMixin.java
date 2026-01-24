package net.capozi.menagerie.mixin;

import com.mojang.authlib.GameProfile;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.server.cca.BoundAccursedComponent;
import net.capozi.menagerie.server.cca.BoundAqueousComponent;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void cancelMovementWhenFrozen(CallbackInfo ci) {
        if (this.hasStatusEffect(EffectInit.CHAINED_EFFECT)) {
            StatusEffectInstance chained = this.getStatusEffect(EffectInit.CHAINED_EFFECT);
            World serverWorld = this.getWorld();
            List<ChainsEntity> chainsNearby = this.getWorld().getEntitiesByClass(ChainsEntity.class, this.getBoundingBox().expand(10.0), entity -> entity.isAlive());
            if (chained == null && chainsNearby.isEmpty()) {
                ChainsEntity chains = new ChainsEntity(EntityInit.BLUE_CHAINS, serverWorld);
                chains.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 12000, 1, false, false, false));
                this.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 12000, 1, false, false, false));
                serverWorld.spawnEntity(chains);
                this.velocityModified = true;
                chains.velocityModified = true;
            }
            this.setVelocity(Vec3d.ZERO);
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
                BoundArtifactComponent accursed = Menagerie.getBoundArtifact().get(this);
                World world = this.getWorld();
                if (world.isRaining() && world.hasRain(pos)) return; // Don't burn in rain
                ItemStack headStack = this.getEquippedStack(EquipmentSlot.HEAD);
                ItemStack chestStack = this.getEquippedStack(EquipmentSlot.CHEST);
                ItemStack legStack = this.getEquippedStack(EquipmentSlot.LEGS);
                ItemStack footStack = this.getEquippedStack(EquipmentSlot.FEET);
                boolean hasHelmet = !headStack.isEmpty();
                if (!hasHelmet) {
                    if(this.getFireTicks() <= 0 && accursed.hasArtifact()) {
                        this.setOnFireFor(20);
                    }
                }
            }
            if (this.getWorld().isRaining() && this.getWorld().hasRain(pos)) {
                BoundAqueousComponent aqueousComponent = Menagerie.getBoundAqueous().get(this);
                if (aqueousComponent.hasAqueous()) {
                    this.addStatusEffect(new StatusEffectInstance(EffectInit.SPEED, 30, 0, false, false, false));
                }
            }
        }
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickEnderChest(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        EnderChestInventory ender = player.getEnderChestInventory();
        for (int i = 0; i < ender.size(); i++) {
            ItemStack stack = ender.getStack(i);
            if (stack.isOf(ItemInit.INCOMPLETE_CONSTRUCT)) {
                if (player.getWorld().random.nextFloat() < 0.0001f) {
                    ender.setStack(i, new ItemStack(ItemInit.REACH_OF_THE_VOID));
                    ender.markDirty();
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