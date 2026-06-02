package net.capozi.menagerie.common.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.client.render.TrickRoomShaders;
import net.capozi.menagerie.common.entity.object.TrickRoomEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class TrickRoomEntityRenderer extends EntityRenderer<TrickRoomEntity> {
    private static final Identifier TEXTURE = Menagerie.identifier("textures/entity/trick_room.png");
    private static final int WHITE_ALPHA = 255;
    private static final float WALL_ALPHA = 1F;
    private static final double VISUAL_INSET = 1.0D / 32.0D;
    private static final double RENDER_QUERY_RANGE = 192.0D;

    public TrickRoomEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(TrickRoomEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public static void renderLast(WorldRenderContext context) {
        if (context.world() == null || context.matrixStack() == null) {
            return;
        }

        Camera camera = context.camera();
        Vec3d cameraPos = camera.getPos();
        Box queryBox = new Box(cameraPos, cameraPos).expand(RENDER_QUERY_RANGE);
        MatrixStack matrices = context.matrixStack();

        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        try {
            for (Entity entity : context.world().getEntities()) {
                if (entity instanceof TrickRoomEntity room
                        && room.hasRoomBounds()
                        && room.getVisibilityBoundingBox().intersects(queryBox)) {
                    float fadeAlpha = room.isFadingOut() ? room.getFadeAlpha(context.tickDelta()) : 0.75f;
                    if (fadeAlpha <= 0.0F) {
                        continue;
                    }
                    Box renderBounds = room.getRoomBounds().contract(VISUAL_INSET);
                    if (!renderShaderBox(room, context.tickDelta(), matrices, renderBounds, fadeAlpha)) {
                        renderWhiteBox(matrices, renderBounds, fadeAlpha);
                    }
                }
            }
        } finally {
            matrices.pop();
        }
    }

    @Override
    public boolean shouldRender(TrickRoomEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public Identifier getTexture(TrickRoomEntity entity) {
        return TEXTURE;
    }

    private static void renderWhiteBox(MatrixStack matrices, Box box, float fadeAlpha) {
        renderDepthPrepass(matrices, box);

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_EQUAL);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, WALL_ALPHA * fadeAlpha);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        emitColorFaces(buffer, matrices.peek().getPositionMatrix(), box);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static boolean renderShaderBox(TrickRoomEntity entity, float tickDelta, MatrixStack matrices, Box box, float fadeAlpha) {
        ShaderProgram shader = TrickRoomShaders.TRICK_ROOM;
        if (shader == null) {
            return false;
        }

        renderDepthPrepass(matrices, box);

        RenderSystem.setShader(() -> shader);
        Uniform time = shader.getUniform("iTime");
        if (time != null) {
            time.set((entity.getWorld().getTime() + tickDelta) / 20.0F);
        }
        Uniform colorModulator = shader.getUniform("ColorModulator");
        if (colorModulator != null) {
            colorModulator.set(1.0F, 1.0F, 1.0F, fadeAlpha);
        }
        Uniform borderOnly = shader.getUniform("BorderOnly");
        if (borderOnly != null) {
            borderOnly.set(0.0F);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_EQUAL);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, fadeAlpha);

        drawTexturedFaces(matrices, box);
        if (borderOnly != null) {
            borderOnly.set(1.0F);
            drawTexturedFaces(matrices, box);
            borderOnly.set(0.0F);
        }

        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    private static void drawTexturedFaces(MatrixStack matrices, Box box) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        emitTexturedFaces(buffer, matrices.peek().getPositionMatrix(), box);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private static void renderDepthPrepass(MatrixStack matrices, Box box) {
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.disableCull();
        RenderSystem.colorMask(false, false, false, false);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        emitPositionFaces(buffer, matrices.peek().getPositionMatrix(), box);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.colorMask(true, true, true, true);
    }

    private static void emitColorFaces(VertexConsumer consumer, Matrix4f matrix, Box box) {
        emitColorQuad(consumer, matrix, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ);
        emitColorQuad(consumer, matrix, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, box.maxX, box.maxY, box.minZ, box.maxX, box.minY, box.minZ);
        emitColorQuad(consumer, matrix, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ, box.maxX, box.minY, box.maxZ);
        emitColorQuad(consumer, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, box.minX, box.maxY, box.minZ, box.minX, box.minY, box.minZ);
        emitColorQuad(consumer, matrix, box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
        emitColorQuad(consumer, matrix, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, box.maxX, box.minY, box.minZ);
    }

    private static void emitTexturedFaces(VertexConsumer consumer, Matrix4f matrix, Box box) {
        // Side faces use one continuous atlas strip around the room perimeter.
        emitTexturedQuad(consumer, matrix,
                box.minX, box.minY, box.minZ, 3.0D, 0.0D,
                box.minX, box.maxY, box.minZ, 3.0D, 1.0D,
                box.minX, box.maxY, box.maxZ, 4.0D, 1.0D,
                box.minX, box.minY, box.maxZ, 4.0D, 0.0D);
        emitTexturedQuad(consumer, matrix,
                box.maxX, box.minY, box.maxZ, 1.0D, 0.0D,
                box.maxX, box.maxY, box.maxZ, 1.0D, 1.0D,
                box.maxX, box.maxY, box.minZ, 2.0D, 1.0D,
                box.maxX, box.minY, box.minZ, 2.0D, 0.0D);
        emitTexturedQuad(consumer, matrix,
                box.minX, box.minY, box.maxZ, 0.0D, 0.0D,
                box.minX, box.maxY, box.maxZ, 0.0D, 1.0D,
                box.maxX, box.maxY, box.maxZ, 1.0D, 1.0D,
                box.maxX, box.minY, box.maxZ, 1.0D, 0.0D);
        emitTexturedQuad(consumer, matrix,
                box.maxX, box.minY, box.minZ, 2.0D, 0.0D,
                box.maxX, box.maxY, box.minZ, 2.0D, 1.0D,
                box.minX, box.maxY, box.minZ, 3.0D, 1.0D,
                box.minX, box.minY, box.minZ, 3.0D, 0.0D);

        // Top and bottom occupy connected atlas panels above and below the first side face.
        emitTexturedQuad(consumer, matrix,
                box.minX, box.maxY, box.maxZ, 0.0D, 1.0D,
                box.minX, box.maxY, box.minZ, 0.0D, 2.0D,
                box.maxX, box.maxY, box.minZ, 1.0D, 2.0D,
                box.maxX, box.maxY, box.maxZ, 1.0D, 1.0D);
        emitTexturedQuad(consumer, matrix,
                box.minX, box.minY, box.minZ, 0.0D, -1.0D,
                box.minX, box.minY, box.maxZ, 0.0D, 0.0D,
                box.maxX, box.minY, box.maxZ, 1.0D, 0.0D,
                box.maxX, box.minY, box.minZ, 1.0D, -1.0D);
    }

    private static void emitPositionFaces(VertexConsumer consumer, Matrix4f matrix, Box box) {
        emitPositionQuad(consumer, matrix, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ);
        emitPositionQuad(consumer, matrix, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, box.maxX, box.maxY, box.minZ, box.maxX, box.minY, box.minZ);
        emitPositionQuad(consumer, matrix, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ, box.maxX, box.minY, box.maxZ);
        emitPositionQuad(consumer, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, box.minX, box.maxY, box.minZ, box.minX, box.minY, box.minZ);
        emitPositionQuad(consumer, matrix, box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
        emitPositionQuad(consumer, matrix, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, box.maxX, box.minY, box.minZ);
    }

    private static void emitColorQuad(VertexConsumer consumer, Matrix4f matrix,
                                      double x1, double y1, double z1,
                                      double x2, double y2, double z2,
                                      double x3, double y3, double z3,
                                      double x4, double y4, double z4) {
        consumer.vertex(matrix, (float) x1, (float) y1, (float) z1).color(255, 255, 255, WHITE_ALPHA).next();
        consumer.vertex(matrix, (float) x2, (float) y2, (float) z2).color(255, 255, 255, WHITE_ALPHA).next();
        consumer.vertex(matrix, (float) x3, (float) y3, (float) z3).color(255, 255, 255, WHITE_ALPHA).next();
        consumer.vertex(matrix, (float) x4, (float) y4, (float) z4).color(255, 255, 255, WHITE_ALPHA).next();
    }

    private static void emitPositionQuad(VertexConsumer consumer, Matrix4f matrix,
                                         double x1, double y1, double z1,
                                         double x2, double y2, double z2,
                                         double x3, double y3, double z3,
                                         double x4, double y4, double z4) {
        consumer.vertex(matrix, (float) x1, (float) y1, (float) z1).next();
        consumer.vertex(matrix, (float) x2, (float) y2, (float) z2).next();
        consumer.vertex(matrix, (float) x3, (float) y3, (float) z3).next();
        consumer.vertex(matrix, (float) x4, (float) y4, (float) z4).next();
    }

    private static void emitTexturedQuad(VertexConsumer consumer, Matrix4f matrix,
                                         double x1, double y1, double z1, double u1, double v1,
                                         double x2, double y2, double z2, double u2, double v2,
                                         double x3, double y3, double z3, double u3, double v3,
                                         double x4, double y4, double z4, double u4, double v4) {
        consumer.vertex(matrix, (float) x1, (float) y1, (float) z1).texture((float) u1, (float) v1).next();
        consumer.vertex(matrix, (float) x2, (float) y2, (float) z2).texture((float) u2, (float) v2).next();
        consumer.vertex(matrix, (float) x3, (float) y3, (float) z3).texture((float) u3, (float) v3).next();
        consumer.vertex(matrix, (float) x4, (float) y4, (float) z4).texture((float) u4, (float) v4).next();
    }
}
