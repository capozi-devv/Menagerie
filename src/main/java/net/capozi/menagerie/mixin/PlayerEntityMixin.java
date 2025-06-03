package net.capozi.menagerie.mixin;

import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({PlayerEntity.class})
public abstract class PlayerEntityMixin extends LivingEntity {
    protected boolean isSubmergedInWater;
    @Shadow public abstract PlayerInventory getInventory();
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
    @ModifyArgs(
            method = {"damage"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private void menagerie$onDamaged(Args args) {
        DamageSource source = (DamageSource) args.get(0);
        float value = (Float)args.get(1);
        if (this.getInventory().contains(ItemInit.MARK_OF_DISSONANCE.getDefaultStack()) && this.isSubmergedInWater && !this.isDead() && this.random.nextInt(6) == 1) {
            args.set(1, value * 2.0F);
        }

    }
    public EntityGroup getGroup() {
        return this.getInventory().contains(ItemInit.MARK_OF_DISSONANCE.getDefaultStack()) ? EntityGroup.UNDEAD : super.getGroup();
    }
    public boolean isUndead() {
        return this.getInventory().contains(ItemInit.MARK_OF_DISSONANCE.getDefaultStack()) ? true : super.isUndead();
    }
    public boolean hurtByWater() {
        return !this.getInventory().contains(ItemInit.MARK_OF_DISSONANCE.getDefaultStack()) && (!this.isInFlowingFluid(FluidTags.WATER) || !this.isSubmergedInWater()) ? super.hurtByWater(): true;
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
