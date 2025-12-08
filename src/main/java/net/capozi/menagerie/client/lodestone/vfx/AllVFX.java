package net.capozi.menagerie.client.lodestone.vfx;

import net.capozi.menagerie.Menagerie;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.*;

public class AllVFX {
    public static void renderSolidPurpleCube(MatrixStack matrices,
                                             VertexConsumerProvider vertexConsumers,
                                             BlockPos position, float size) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getSolid());

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

        float r = 0.5f, g = 0.0f, b = 0.8f, a = 0.7f; // Semi-transparent

        float minX = position.getX() - size/2;
        float minY = position.getY() - size/2;
        float minZ = position.getZ() - size/2;
        float maxX = position.getX() + size/2;
        float maxY = position.getY() + size/2;
        float maxZ = position.getZ() + size/2;

        drawQuad(vertexConsumer, matrix, normalMatrix, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, // Front
                r, g, b, a);
    }

    private static void drawQuad(VertexConsumer consumer, Matrix4f matrix, Matrix3f normalMatrix,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3,
                                 float x4, float y4, float z4,
                                 float r, float g, float b, float a) {

        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(normalMatrix, 0, 0, 1).light(1).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).next();
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(normalMatrix, 0, 0, 1).light(1).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).next();
        consumer.vertex(matrix, x3, y3, z3).color(r, g, b, a).normal(normalMatrix, 0, 0, 1).light(1).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).next();
        consumer.vertex(matrix, x4, y4, z4).color(r, g, b, a).normal(normalMatrix, 0, 0, 1).light(1).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).next();
    }
    public static boolean shouldRenderCube = false;
    public static BlockPos cubePosition = BlockPos.ORIGIN;

    // Call this method when you want to show the cube
    public static void showCubeAt(BlockPos pos) {
        shouldRenderCube = true;
        cubePosition = pos;
    }

    public static void hideCube() {
        shouldRenderCube = false;
    }
    private static RenderTypeToken getRenderTypeToken() {
        return RenderTypeToken.createToken(new Identifier(Menagerie.MOD_ID, "textures/entity/beam3.png"));
    }
    private static long flashStartTime = 0;
    private static final long FLASH_DURATION_MS = 13000;
    public static void triggerFlash() {
        flashStartTime = System.currentTimeMillis();
    }
    private static boolean isFlashing() {
        return System.currentTimeMillis() - flashStartTime < 13000;
    }
    private static final LodestoneRenderType RENDER_LAYER = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE.applyAndCache(getRenderTypeToken());
    public static void renderObelisk(MatrixStack matrixStack, Vec3d pos) {
        matrixStack.scale(1f, 1f, 1f);
        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld();
        triggerFlash();
        float progress = (System.currentTimeMillis() - flashStartTime) / (float) FLASH_DURATION_MS;
        float alpha = 1.0f - progress;
        if (alpha < 0f) alpha = 0f;
        matrixStack.push();
        matrixStack.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        if (isFlashing()) {
            builder.replaceBufferSource(RenderHandler.DELAYED_RENDER.getTarget())
                    .setRenderType(RENDER_LAYER)
                    .setColor(new Color(0, 255, 244))
                    .setAlpha(0.9f)
                    .setLight(200)
                    .renderBeam(matrix4f, pos.add(0.5, 0, 0.5), pos.add(0.5,300,0.5), 9);
        }
        matrixStack.pop();
    }
}
