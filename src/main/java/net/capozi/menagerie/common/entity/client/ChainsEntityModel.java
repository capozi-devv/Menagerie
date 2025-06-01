package net.capozi.menagerie.common.entity.client;

import net.capozi.menagerie.common.entity.animation.ChainsEntityAnimation;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class ChainsEntityModel<T extends ChainsEntity> extends SinglePartEntityModel<T> {
	private final ModelPart base;
	public ChainsEntityModel(ModelPart root) {
		this.base = root.getChild("base");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData Chain1 = base.addChild("Chain1", ModelPartBuilder.create(), ModelTransform.of(0.0F, -18.5F, 0.0F, 0.0F, 0.0F, 0.3927F));

		ModelPartData Chain1rot = Chain1.addChild("Chain1rot", ModelPartBuilder.create().uv(42, 2).mirrored().cuboid(10.0F, -2.5F, -10.0F, 0.0F, 5.0F, 20.0F, new Dilation(0.0F)).mirrored(false)
		.uv(2, 22).cuboid(-10.0F, -2.5F, -10.0F, 20.0F, 5.0F, 0.0F, new Dilation(0.0F))
		.uv(42, 22).cuboid(-10.0F, -2.5F, 10.0F, 20.0F, 5.0F, 0.0F, new Dilation(0.0F))
		.uv(2, 2).mirrored().cuboid(-10.0F, -2.5F, -10.0F, 0.0F, 5.0F, 20.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData Chain2 = base.addChild("Chain2", ModelPartBuilder.create(), ModelTransform.of(0.0F, -18.5F, 0.0F, 0.0F, 0.0F, -0.3927F));

		ModelPartData Chain1rot2 = Chain2.addChild("Chain1rot2", ModelPartBuilder.create().uv(42, 2).mirrored().cuboid(10.0F, -2.5F, -10.0F, 0.0F, 5.0F, 20.0F, new Dilation(0.0F)).mirrored(false)
		.uv(2, 22).cuboid(-10.0F, -2.5F, -10.0F, 20.0F, 5.0F, 0.0F, new Dilation(0.0F))
		.uv(42, 22).cuboid(-10.0F, -2.5F, 10.0F, 20.0F, 5.0F, 0.0F, new Dilation(0.0F))
		.uv(2, 2).mirrored().cuboid(-10.0F, -2.5F, -10.0F, 0.0F, 5.0F, 20.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	@Override
	public ModelPart getPart() {
		return base;
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		this.updateAnimation(entity.IdleAnimationState, ChainsEntityAnimation.Idle, animationProgress, 1f);
	}

}