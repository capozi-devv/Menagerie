package net.capozi.menagerie.common.render;

import net.capozi.menagerie.Menagerie;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer CHAINS =
            new EntityModelLayer(new Identifier(Menagerie.MOD_ID, "chains"), "main");


    private static class EmissiveLayer<T extends MobEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
        private static final Identifier CHAINS_EMISSIVE = new Identifier(Menagerie.MOD_ID, "textures/entity/chains_emissive.png");

        public EmissiveLayer(FeatureRendererContext<T, M> context) {
            super(context);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEyes(CHAINS_EMISSIVE));
            getContextModel().render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}



