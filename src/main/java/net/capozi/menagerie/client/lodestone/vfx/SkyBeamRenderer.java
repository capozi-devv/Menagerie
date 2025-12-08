package net.capozi.menagerie.client.lodestone.vfx;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SkyBeamRenderer {

    public static void render(MatrixStack matrices, VertexConsumerProvider provider, Identifier texture,
                              float tiltAngleDeg, float height, float halfWidth, RenderOptions options) {

        matrices.push();

        var cam = MinecraftClient.getInstance().gameRenderer.getCamera();
        float yaw = cam.getYaw();

        // Apply SPIN rotation around Y-axis
        if (options.animation == RenderOptions.AnimationStyle.SPIN) {
            float time = MinecraftClient.getInstance().world.getTime()
                    + MinecraftClient.getInstance().getTickDelta();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 4f * options.animationSpeed));
        } else {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        }

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(tiltAngleDeg));

        // Use additive layer
        VertexConsumer cons = provider.getBuffer(RenderLayer.getEntityTranslucent(texture)); // replace with additive layer if defined
        MatrixStack.Entry e = matrices.peek();

        float time = MinecraftClient.getInstance().world.getTime()
                + MinecraftClient.getInstance().getTickDelta();

        // UV scroll
        float scroll = time * options.uvSpeed;
        float wobbleU = 0f;

        if (options.animation == RenderOptions.AnimationStyle.WOBBLE) {
            wobbleU = (float) Math.sin(time * 0.15f * options.animationSpeed) * 0.08f;
        }

        float x1 = -halfWidth * options.size;
        float x2 = halfWidth * options.size;
        float zTop = height * options.size;
        float vRepeat = height * 0.10f;

        // Alpha calculation
        float alphaBottom = options.minAlpha * 255f;
        float alphaTop = options.minAlpha * 255f;

        if (options.fadeInTicks > 0)
            alphaBottom = MathHelper.clamp(time / options.fadeInTicks * 255f, options.minAlpha * 255f, options.maxAlpha * 255f);
        else
            alphaBottom = options.maxAlpha * 255f;

        if (options.fadeOutTicks > 0)
            alphaTop = MathHelper.clamp((options.fadeOutTicks - (time % options.fadeOutTicks)) / options.fadeOutTicks * 255f,
                    options.minAlpha * 255f, options.maxAlpha * 255f);
        else
            alphaTop = options.maxAlpha * 255f;

        if (options.animation == RenderOptions.AnimationStyle.PULSE) {
            float pulse = (MathHelper.sin(time * 0.1f * options.animationSpeed) * 0.5f + 0.5f);
            alphaBottom *= pulse;
            alphaTop *= pulse;
        }

        // Draw quad
        put(cons, e, x1, 0, 0, wobbleU + 0, scroll, alphaBottom);
        put(cons, e, x1, 0, zTop, wobbleU + 0, scroll + vRepeat, alphaTop);
        put(cons, e, x2, 0, zTop, wobbleU + 1, scroll + vRepeat, alphaTop);
        put(cons, e, x2, 0, 0, wobbleU + 1, scroll, alphaBottom);

        matrices.pop();
    }

    private static void put(VertexConsumer cons, MatrixStack.Entry e,
                            float x, float y, float z,
                            float u, float v,
                            float alpha) {

        Matrix4f pos = e.getPositionMatrix();
        Matrix3f norm = e.getNormalMatrix();

        int a = MathHelper.clamp((int) alpha, 0, 255);

        cons.vertex(pos, x, y, z)
                .color(255, 255, 255, a)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(norm, 0f, 1f, 0f)
                .next();
    }
}
