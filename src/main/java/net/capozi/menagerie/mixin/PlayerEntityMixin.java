package net.capozi.menagerie.mixin;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.server.cca.BoundAccursedComponent;
import net.capozi.menagerie.server.cca.BoundAccursedComponentImpl;
import net.capozi.menagerie.server.cca.BoundAqueousComponent;
import net.capozi.menagerie.server.cca.BoundArtifactComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected boolean isSubmergedInWater;
    @Shadow public abstract boolean isPushedByFluids();

    @Shadow
    protected abstract boolean isOnSoulSpeedBlock();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
       super(entityType, world);
    }
    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void menagerie$reduceAttackRange(Entity target, CallbackInfo ci) {
        if ((Object)this instanceof PlayerEntity marked) {
            BoundAqueousComponent component = Menagerie.getBoundAqueous().get(marked);
            if (component.hasAqueous()) {
                if (!isSubmergedInWater) {
                    double maxReach = 4.0D + marked.getAttributes().getValue(ReachEntityAttributes.ATTACK_RANGE); // reduced reach
                    double maxReachSq = maxReach * maxReach;
                    if (marked.squaredDistanceTo(target) > maxReachSq) {
                        ci.cancel(); // prevents the attack entirely
                    }
                }
            }
        }
    }
    @ModifyArgs(method = {"damage"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private void menagerie$onDamaged(Args args) {
        DamageSource source = (DamageSource) args.get(0);
        float value = (Float) args.get(1);
        BoundAccursedComponent component = Menagerie.getBoundAccursed().get(this);
        if (component.hasAccursed() && (this.isSubmergedInWater() || isInFlowingFluid(FluidTags.WATER)) && !this.isDead() && this.random.nextInt(6) == 1) {
            args.set(1, value * 2.0F);
        }
    }
    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        BoundArtifactComponent component = Menagerie.getBoundArtifact().get(this);
        if (!component.hasArtifact()) {
            return super.canHaveStatusEffect(effect);
        } else {
            return effect.getEffectType() == StatusEffects.INSTANT_DAMAGE || effect.getEffectType() == StatusEffects.WITHER || effect.getEffectType() == StatusEffects.INSTANT_HEALTH;
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
        BoundAccursedComponent component = Menagerie.getBoundAccursed().get(this);
        return component.hasAccursed() ? true : super.hurtByWater();
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
