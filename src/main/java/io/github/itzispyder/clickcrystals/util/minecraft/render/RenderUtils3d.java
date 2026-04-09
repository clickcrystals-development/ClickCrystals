package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import static io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils.drawBuffer;
import static io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils.getBuffer;

public class RenderUtils3d {

    public static void drawLine(PoseStack matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        BufferBuilder buf = getBuffer(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f mat = matrices.last().pose();

        buf.addVertex(mat, (float)x1, (float)y1, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y2, (float)z2).setColor(color);

        drawBuffer(buf, ClickCrystalsRenderLayers.LINES);
    }

    public static void drawFlatLine(PoseStack matrices, double x1, double y1, double z1, double x2, double y2, double z2, double width, int color) {
        int colorQuat = (0x40 << 24) | (0x00FFFFFF & color);
        int colorHalf = (0x80 << 24) | (0x00FFFFFF & color);
        int colorFull = (0xFF << 24) | (0x00FFFFFF & color);

        double half = width * 0.5;
        Vec3 from = new Vec3(x1, y1, z1);
        Vec3 to = new Vec3(x2, y2, z2);
        Vec3 dir = from.subtract(to).normalize();

        Vec3 v1 = MathUtils.forward(from, MathUtils.rotate(dir, from, 0, -90F), half);
        Vec3 v2 = MathUtils.forward(to, MathUtils.rotate(dir, to, 0, -90F), half);
        Vec3 v3 = MathUtils.forward(to, MathUtils.rotate(dir, to, 0, 90F), half);
        Vec3 v4 = MathUtils.forward(from, MathUtils.rotate(dir, from, 0, 90F), half);

        // rect
        BufferBuilder buf = getBuffer(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f mat = matrices.last().pose();

        buf.addVertex(mat, (float)v1.x, (float)v1.y, (float)v1.z).setColor(colorQuat);
        buf.addVertex(mat, (float)v2.x, (float)v2.y, (float)v2.z).setColor(colorQuat);
        buf.addVertex(mat, (float)v3.x, (float)v3.y, (float)v3.z).setColor(colorQuat);
        buf.addVertex(mat, (float)v4.x, (float)v4.y, (float)v4.z).setColor(colorQuat);

        drawBuffer(buf, ClickCrystalsRenderLayers.QUADS);

        // lines
        drawLine(matrices, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, colorFull);
        drawLine(matrices, v3.x, v3.y, v3.z, v4.x, v4.y, v4.z, colorFull);
//        drawLine(matrices, v1.x, v1.y, v1.z, v4.x, v4.y, v4.z, colorHalf);
//        drawLine(matrices, v3.x, v3.y, v3.z, v2.x, v2.y, v2.z, colorHalf);
    }

    public static void fillCube(PoseStack matrices, double x, double y, double z, int color) {
        Matrix4f mat = matrices.last().pose();
        BufferBuilder buf = getBuffer(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float diff = 0.0001F; // fix z-fighting

        buf.addVertex(mat, (float)(x + 0), (float)(y + 0) - diff, (float)(z + 0)).setColor(color); // bottom
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0) - diff, (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0) - diff, (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 0) - diff, (float)(z + 1)).setColor(color);

        buf.addVertex(mat, (float)(x + 0), (float)(y + 1) + diff, (float)(z + 0)).setColor(color); // top
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1) + diff, (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1) + diff, (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1) + diff, (float)(z + 1)).setColor(color);

        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 0) - diff).setColor(color); // front
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 0) - diff).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 0) - diff).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 0) - diff).setColor(color);

        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 1) + diff).setColor(color); // back
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 1) + diff).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 1) + diff).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 1) + diff).setColor(color);

        buf.addVertex(mat, (float)(x + 0) - diff, (float)(y + 0), (float)(z + 0)).setColor(color); // left
        buf.addVertex(mat, (float)(x + 0) - diff, (float)(y + 1), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 0) - diff, (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0) - diff, (float)(y + 0), (float)(z + 1)).setColor(color);

        buf.addVertex(mat, (float)(x + 1) + diff, (float)(y + 0), (float)(z + 0)).setColor(color); // right
        buf.addVertex(mat, (float)(x + 1) + diff, (float)(y + 1), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1) + diff, (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 1) + diff, (float)(y + 0), (float)(z + 1)).setColor(color);

        drawBuffer(buf, ClickCrystalsRenderLayers.QUADS);
    }

    public static void fillBox(PoseStack matrices, AABB box, int color) {
        fillRectPrism(matrices, (float)box.minX, (float)box.minY, (float)box.minZ, (float)box.maxX, (float)box.maxY, (float)box.maxZ, color);
    }

    public static void fillRectPrism(PoseStack matrices, float x1, float y1, float z1, float x2, float y2, float z2, int color, boolean cull) {
        Matrix4f mat = matrices.last().pose();
        BufferBuilder buf = getBuffer(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        buf.addVertex(mat, x1, y1, z1).setColor(color); // bottom
        buf.addVertex(mat, x2, y1, z1).setColor(color);
        buf.addVertex(mat, x2, y1, z2).setColor(color);
        buf.addVertex(mat, x1, y1, z2).setColor(color);

        buf.addVertex(mat, x1, y2, z1).setColor(color); // top
        buf.addVertex(mat, x2, y2, z1).setColor(color);
        buf.addVertex(mat, x2, y2, z2).setColor(color);
        buf.addVertex(mat, x1, y2, z2).setColor(color);

        buf.addVertex(mat, x1, y1, z1).setColor(color); // front
        buf.addVertex(mat, x2, y1, z1).setColor(color);
        buf.addVertex(mat, x2, y2, z1).setColor(color);
        buf.addVertex(mat, x1, y2, z1).setColor(color);

        buf.addVertex(mat, x1, y1, z2).setColor(color); // back
        buf.addVertex(mat, x2, y1, z2).setColor(color);
        buf.addVertex(mat, x2, y2, z2).setColor(color);
        buf.addVertex(mat, x1, y2, z2).setColor(color);

        buf.addVertex(mat, x1, y1, z1).setColor(color); // left
        buf.addVertex(mat, x1, y2, z1).setColor(color);
        buf.addVertex(mat, x1, y2, z2).setColor(color);
        buf.addVertex(mat, x1, y1, z2).setColor(color);

        buf.addVertex(mat, x2, y1, z1).setColor(color); // right
        buf.addVertex(mat, x2, y2, z1).setColor(color);
        buf.addVertex(mat, x2, y2, z2).setColor(color);
        buf.addVertex(mat, x2, y1, z2).setColor(color);

        drawBuffer(buf, cull ? ClickCrystalsRenderLayers.QUADS_CULL : ClickCrystalsRenderLayers.QUADS);
    }

    public static void fillRectPrism(PoseStack matrices, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
        fillRectPrism(matrices, x1, y1, z1, x2, y2, z2, color, false);
    }

    public static void fillCyl(PoseStack matrices, double x, double y, double z, double radius, double height, int color) {
        BufferBuilder buf = getBuffer(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f mat = matrices.last().pose();

        for (int i = 0; i <= 360; i += 24) {
            float angle = (float)Math.toRadians(i);
            float cx = (float)(x + Mth.cos(angle) * radius);
            float cz = (float)(z + Mth.sin(angle) * radius);
            buf.addVertex(mat, cx, (float)y, cz).setColor(color);
            buf.addVertex(mat, cx, (float)(y + height), cz).setColor(color);
        }

        drawBuffer(buf, ClickCrystalsRenderLayers.TRI_STRIP);
    }

    public static void fillCylGradient(PoseStack matrices, double x, double y, double z, double radius, double height, int colorBottom, int colorTop) {
        BufferBuilder buf = getBuffer(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f mat = matrices.last().pose();

        for (int i = 0; i <= 360; i += 30) {
            float angle = (float)Math.toRadians(i);
            float cx = (float)(x + Mth.cos(angle) * radius);
            float cz = (float)(z + Mth.sin(angle) * radius);
            buf.addVertex(mat, cx, (float)y, cz).setColor(colorBottom);
            buf.addVertex(mat, cx, (float)(y + height), cz).setColor(colorTop);
        }

        drawBuffer(buf, ClickCrystalsRenderLayers.TRI_STRIP_CULL);
    }

    public static void drawCube(PoseStack matrices, double x, double y, double z, int color) {
        Matrix4f mat = matrices.last().pose();
        BufferBuilder buf = getBuffer(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 0)).setColor(color); // bottom
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 0)).setColor(color);

        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 0)).setColor(color); // top
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 0)).setColor(color);

        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 0)).setColor(color); // pillars
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 0)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 0), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 1), (float)(y + 1), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 0), (float)(z + 1)).setColor(color);
        buf.addVertex(mat, (float)(x + 0), (float)(y + 1), (float)(z + 1)).setColor(color);

        drawBuffer(buf, ClickCrystalsRenderLayers.LINES);
    }

    public static void renderBlock(PoseStack matrices, Vec3 block, int color) {
        var c = new Color(color);
        fillCube(matrices, block.x(), block.y(), block.z(), c.getHex());
        drawCube(matrices, block.x(), block.y(), block.z(), c.getHexOpaque());
    }

    public static void drawRectPrism(PoseStack matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color, boolean cull) {
        Matrix4f mat = matrices.last().pose();
        BufferBuilder buf = getBuffer(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        AABB box = new AABB(x1, y1, z1, x2, y2, z2);

        x1 = box.minX;
        y1 = box.minY;
        z1 = box.minZ;
        x2 = box.maxX;
        y2 = box.maxY;
        z2 = box.maxZ;

        buf.addVertex(mat, (float)x1, (float)y1, (float)z1).setColor(color); // bottom
        buf.addVertex(mat, (float)x2, (float)y1, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y1, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y1, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y1, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y1, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y1, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y1, (float)z1).setColor(color);

        buf.addVertex(mat, (float)x1, (float)y2, (float)z1).setColor(color); // top
        buf.addVertex(mat, (float)x2, (float)y2, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y2, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y2, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y2, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y2, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y2, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y2, (float)z1).setColor(color);

        buf.addVertex(mat, (float)x1, (float)y1, (float)z1).setColor(color); // pillars
        buf.addVertex(mat, (float)x1, (float)y2, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y1, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y2, (float)z1).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y1, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x2, (float)y2, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y1, (float)z2).setColor(color);
        buf.addVertex(mat, (float)x1, (float)y2, (float)z2).setColor(color);

        drawBuffer(buf, cull ? ClickCrystalsRenderLayers.LINES_CULL : ClickCrystalsRenderLayers.LINES);
    }

    public static void drawRectPrism(PoseStack matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        drawRectPrism(matrices, x1, y1, z1, x2, y2, z2, color, false);
    }

    public static void renderPlayerThingy(PoseStack matrices, double x, double y, double z, float pitch, float yaw, int color) {
        double pixelsToBlocks = 0.05625; // I used a calculator for this (1.8 / 32) or (player height blocks / player height pixels)
        double b12 = 12 * pixelsToBlocks;
        double b2 = 2 * pixelsToBlocks;
        double b4 = 4 * pixelsToBlocks;
        double b8 = 8 * pixelsToBlocks;
        double b0 = 0 * pixelsToBlocks;

        matrices.pushPose();
        matrices.rotateAround(Axis.YN.rotationDegrees(yaw), (float)x, (float)y, (float)z);

        drawRectPrism(matrices, x - b4, y + b0, z - b2, x + b0, y + b12, z + b2, color); // left leg
        drawRectPrism(matrices, x + b4, y + b0, z - b2, x + b0, y + b12, z + b2, color); // right leg

        drawRectPrism(matrices, x - b4, y + b12, z - b2, x + b4, y + b12 + b12, z + b2, color); // body

        drawRectPrism(matrices, x - b8, y + b12, z - b2, x - b4, y + b12 + b12, z + b2, color); // left arm
        drawRectPrism(matrices, x + b8, y + b12, z - b2, x + b4, y + b12 + b12, z + b2, color); // right arm

        matrices.rotateAround(Axis.XP.rotationDegrees(pitch), (float)x, (float)(y + b12 + b12), (float)z);

        drawRectPrism(matrices, x - b4, y + b12 + b12, z - b4, x + b4, y + b12 + b12 + b8, z + b4, color); // head

        matrices.popPose();
    }
}