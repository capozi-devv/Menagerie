package net.capozi.menagerie.common.entity.client;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.client.render.ModModelLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class ChainsRenderer extends MobEntityRenderer<ChainsEntity, ChainsEntityModel<ChainsEntity>> {
    private static final Identifier BASE_TEXTURE = new Identifier(Menagerie.MOD_ID, "textures/entity/chains.png");

    public ChainsRenderer(EntityRendererFactory.Context context) {
        super(context, new ChainsEntityModel<>(context.getPart(ModModelLayers.CHAINS)), 0.5f);
        this.addFeature(new EmissiveLayer<>(this));
    }

    @Override
    public Identifier getTexture(ChainsEntity entity) {
        return BASE_TEXTURE;
    }

    private static class EmissiveLayer<T extends MobEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
        private static final Identifier EMISSIVE_TEXTURE = new Identifier(Menagerie.MOD_ID, "textures/entity/chains_emissive.png");

        public EmissiveLayer(FeatureRendererContext<T, M> context) {
            super(context);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
                           float limbAngle, float limbDistance, float tickDelta,
                           float animationProgress, float headYaw, float headPitch) {

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEyes(EMISSIVE_TEXTURE));

            this.getContextModel().render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV,
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
