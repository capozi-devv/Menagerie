package net.capozi.menagerie.mixin.client;

import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }
    @Shadow
    abstract void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model);
    @Shadow
    abstract A getModel(EquipmentSlot slot);
    @Inject(method = "setVisible", at = @At("TAIL"))
    private void menagerie$setVisible(A bipedModel, EquipmentSlot slot, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        GameOptions options = MinecraftClient.getInstance().options;
        switch (slot) {
            case CHEST:
                if (player.getMainHandStack().isOf(ItemInit.PUNCH_UP_STAR)) {
                    if (options.getMainArm().getValue() == Arm.LEFT) {
                        bipedModel.leftArm.visible = false;
                    } else {
                        bipedModel.rightArm.visible = false;
                    }
                }
                if (player.getOffHandStack().isOf(ItemInit.PUNCH_UP_STAR)) {
                    if (options.getMainArm().getValue() == Arm.LEFT) {
                        bipedModel.rightArm.visible = false;
                    } else {
                        bipedModel.leftArm.visible = false;
                    }
                }
        }
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.CHEST, light, this.getModel(EquipmentSlot.CHEST));
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.LEGS, light, this.getModel(EquipmentSlot.LEGS));
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.FEET, light, this.getModel(EquipmentSlot.FEET));
        this.renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.HEAD, light, this.getModel(EquipmentSlot.HEAD));
    }
}
