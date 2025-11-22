package net.capozi.menagerie.mixin;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.network.BoundAqueousComponent;
import net.capozi.menagerie.common.network.BoundArtifactComponent;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({PlayerEntity.class})
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract boolean isPushedByFluids();
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    public boolean isTrapped = false;
    public boolean isTrapped() {
        return isTrapped;
    }
    public void setTrapped(boolean trapped) {
        this.isTrapped = trapped;
    }
    @ModifyArgs(method = {"damage"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private void menagerie$onDamaged(Args args) {
        DamageSource source = (DamageSource) args.get(0);
        float value = (Float) args.get(1);
        BoundArtifactComponent component = Menagerie.getBoundArtifact().get(this);
        if (component.hasArtifact() && (this.isSubmergedInWater() || isInFlowingFluid(FluidTags.WATER)) && !this.isDead() && this.random.nextInt(6) == 1) {
            args.set(1, value * 2.0F);
        }
    }
    @Override
    public EntityGroup getGroup() {
        BoundArtifactComponent component = Menagerie.getBoundArtifact().get(this);
        return component.hasArtifact() ? EntityGroup.UNDEAD : super.getGroup();
    }
    @Override
    public boolean isUndead() {
        BoundArtifactComponent component = Menagerie.getBoundArtifact().get(this);
        return component.hasArtifact() || super.isUndead();
    }
    @Override
    public boolean hurtByWater() {
        BoundArtifactComponent component = Menagerie.getBoundArtifact().get(this);
        return component.hasArtifact() ? true : super.hurtByWater();
    }
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void tickDissonanceFire(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getTypeRegistryEntry().matchesKey(DamageTypes.LAVA) || source.getTypeRegistryEntry().matchesKey(DamageTypes.ON_FIRE) || source.getTypeRegistryEntry().matchesKey(DamageTypes.IN_FIRE)) {
            cir.cancel();
        }
    }
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void playerBiomeTicker(CallbackInfo ci) {
        if (this.getWorld().isClient()) return;
        BoundAqueousComponent component = Menagerie.getBoundAqueous().get(this);
        System.out.println("PLAYER TICKING (hasAqueous=" + component.hasAqueous() + ")");
        if (component.hasAqueous()) {
            RegistryEntry<Biome> biomeEntry = this.getWorld().getBiome(this.getBlockPos());
            RegistryKey<Biome> biomeKey = biomeEntry.getKey().orElse(null);
            if (BiomeKeys.DESERT.equals(biomeKey) || BiomeKeys.ERODED_BADLANDS.equals(biomeKey) || BiomeKeys.BADLANDS.equals(biomeKey) || BiomeKeys.SAVANNA.equals(biomeKey) || BiomeKeys.SAVANNA_PLATEAU.equals(biomeKey) || BiomeKeys.WINDSWEPT_SAVANNA.equals(biomeKey)) {
                if(this.getFireTicks() <= 0) {
                    this.setOnFireFor(20);
                }
            }
        }
    }
    private boolean isInFlowingFluid(TagKey<Fluid> tag) {
        if (this.isRegionUnloaded()) {
            return false;
        } else {
            Box box = this.getBoundingBox().contract(0.001);
            int i = MathHelper.floor(box.minX);
            int j = MathHelper.ceil(box.maxX);
            int k = MathHelper.floor(box.minY);
            int l = MathHelper.ceil(box.maxY);
            int m = MathHelper.floor(box.minZ);
            int n = MathHelper.ceil(box.maxZ);
            double d = (double)0.0F;
            boolean bl = this.isPushedByFluids();
            boolean bl2 = false;
            Vec3d vec3d = Vec3d.ZERO;
            int o = 0;
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for(int p = i; p < j; ++p) {
                for(int q = k; q < l; ++q) {
                    for(int r = m; r < n; ++r) {
                        mutable.set(p, q, r);
                        FluidState fluidState = this.getWorld().getFluidState(mutable);
                        double e;
                        if (fluidState.isIn(tag) && (e = (double)((float)q + fluidState.getHeight(this.getWorld(), mutable))) >= box.minY && !fluidState.isEqualAndStill(Fluids.WATER)) {
                            bl2 = true;
                            d = Math.max(e - box.minY, d);
                            if (bl) {
                                Vec3d vec3d2 = fluidState.getVelocity(this.getWorld(), mutable);
                                if (d < 0.4) {
                                    vec3d2 = vec3d2.multiply(d);
                                }
                                vec3d = vec3d.add(vec3d2);
                                ++o;
                            }
                        }
                    }
                }
            }
            return bl2;
        }
    }
}
