package net.capozi.menagerie.client.lodestone.vfx;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

public class AllVFX {
    public static void renderCube(MatrixStack matrices, VertexConsumerProvider provider, Box box,float r, float g, float b, float a, int light) {
        VertexConsumer consumer = provider.getBuffer(RenderLayer.getTranslucent());
        MatrixStack.Entry entry = matrices.peek();
        float minX = (float) box.minX;
        float minY = (float) box.minY;
        float minZ = (float) box.minZ;
        float maxX = (float) box.maxX;
        float maxY = (float) box.maxY;
        float maxZ = (float) box.maxZ;
        addDoubleSidedQuad(consumer, entry,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                r, g, b, a, light);
        addDoubleSidedQuad(consumer, entry,
                maxX, minY, minZ,
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                r, g, b, a, light);
        addDoubleSidedQuad(consumer, entry,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                minX, maxY, minZ,
                r, g, b, a, light);
        addDoubleSidedQuad(consumer, entry,
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                minX, minY, maxZ,
                r, g, b, a, light);
        addDoubleSidedQuad(consumer, entry,
                maxX, minY, maxZ,
                maxX, minY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ,
                r, g, b, a, light);
        addDoubleSidedQuad(consumer, entry,
                minX, minY, minZ,
                minX, minY, maxZ,
                minX, maxY, maxZ,
                minX, maxY, minZ,
                r, g, b, a, light);
    }
    private static void addQuad(VertexConsumer consumer, MatrixStack.Entry entry,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float x3, float y3, float z3,
                                float x4, float y4, float z4,
                                float r, float g, float b, float a, int light) {
        float nx = (y2 - y1) * (z3 - z2) - (z2 - z1) * (y3 - y2);
        float ny = (z2 - z1) * (x3 - x2) - (x2 - x1) * (z3 - z2);
        float nz = (x2 - x1) * (y3 - y2) - (y2 - y1) * (x3 - x2);
        float len = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= len;
        ny /= len;
        nz /= len;
        addVertex(consumer, entry, x1, y1, z1, r, g, b, a, 0, 0, nx, ny, nz, light);
        addVertex(consumer, entry, x2, y2, z2, r, g, b, a, 1, 0, nx, ny, nz, light);
        addVertex(consumer, entry, x3, y3, z3, r, g, b, a, 1, 1, nx, ny, nz, light);
        addVertex(consumer, entry, x4, y4, z4, r, g, b, a, 0, 1, nx, ny, nz, light);
    }
    private static void addVertex(VertexConsumer consumer, MatrixStack.Entry entry,
                                  float x, float y, float z,
                                  float r, float g, float b, float a,
                                  float u, float v,
                                  float nx, float ny, float nz,
                                  int light) {
        consumer.vertex(entry.getPositionMatrix(), x, y, z)
                .color(r, g, b, a)
                .texture(u, v)
                .light(light)
                .normal(entry.getNormalMatrix(), nx, ny, nz);
    }
    private static void addDoubleSidedQuad(VertexConsumer consumer, MatrixStack.Entry entry, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float r, float g, float b, float a, int light) {
        addQuad(consumer, entry,
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3,
                x4, y4, z4,
                r, g, b, a, light);
        addQuad(consumer, entry,
                x4, y4, z4,
                x3, y3, z3,
                x2, y2, z2,
                x1, y1, z1,
                r, g, b, a, light);
    }
}
