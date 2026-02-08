package net.capozi.menagerie.common.entity.client;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.client.lodestone.particle.AllParticles;
import net.capozi.menagerie.client.lodestone.vfx.AllVFX;
import net.capozi.menagerie.client.lodestone.vfx.RenderOptions;
import net.capozi.menagerie.client.lodestone.vfx.SkyBeamRenderer;
import net.capozi.menagerie.client.render.FlashOverlayRenderer;
import net.capozi.menagerie.common.entity.object.CircleBeamEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class CircleBeamRenderer extends EntityRenderer<CircleBeamEntity> {
    public static boolean shouldRender = false;
    public CircleBeamRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    @Override
    public Identifier getTexture(CircleBeamEntity entity) {
        return new Identifier(Menagerie.MOD_ID, "textures/entity/beam2.png");
    }
    public static long flashStartTime = 0;
    public static final long FLASH_DURATION_MS = 13000;
    public static void triggerFlash() {
        flashStartTime = System.currentTimeMillis();
    }
    public static boolean isFlashing() {
        return System.currentTimeMillis() - flashStartTime < 13000;
    }
    @Override
    public void render(CircleBeamEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        RenderOptions options = new RenderOptions().animation(RenderOptions.AnimationStyle.WOBBLE, 1f).uvSpeed(3f);
        float progress = (System.currentTimeMillis() - flashStartTime) / (float) FLASH_DURATION_MS;
        if (shouldRender) {
            //SkyBeamRenderer.render(matrixStack, vertexConsumerProvider, getTexture(mobEntity), 270, 300, 4f, options);
            AllVFX.renderObelisk(matrixStack, Vec3d.ofCenter(mobEntity.getBlockPos()));
            AllParticles.glowAura(mobEntity.getWorld(), Vec3d.ofCenter(mobEntity.getBlockPos()));
            if (!FlashOverlayRenderer.isFlashing()) {
                FlashOverlayRenderer.triggerFlash();
            }
        }
    }
    @Override
    public boolean shouldRender(CircleBeamEntity mobEntity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
