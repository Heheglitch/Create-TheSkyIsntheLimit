package net.ctslteam.ctsl.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import foundry.veil.api.client.render.MatrixStack;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class CelestialVeilRenderer {
    private static final Vec3 DEBUG_REAL_POSITION = new Vec3(80.0, 100.0, 0.0);
    private static final double RENDER_DISTANCE_FROM_CAMERA = 120.0D;

    private static final double DEBUG_RADIUS = 8.0D;
    private static final double MIN_VISUAL_SIZE = 0.0D;

    private CelestialVeilRenderer() {
    }

    public static void render(
            MultiBufferSource bufferSource,
            MatrixStack matrixStack,
            DeltaTracker deltaTracker,
            Camera camera
    ) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.getConnection() == null) {
            return;
        }

        Vec3 cameraPos = camera.getPosition();
        VertexConsumer consumer = bufferSource.getBuffer(CelestialRenderTypes.CELESTIALS);

        Vec3 renderedPos = computeRenderedPosition(cameraPos, DEBUG_REAL_POSITION);
        float visualSize = (float) computeVisualSize(cameraPos, DEBUG_REAL_POSITION, DEBUG_RADIUS);

        if (mc.level.getGameTime() % 40 == 0) {
            double realDistance = cameraPos.distanceTo(DEBUG_REAL_POSITION);
            CreateTheSkyIsnttheLimit.LOGGER.info(
                    "Debug cube realDistance={} visualSize={} cameraPos={} realPos={}",
                    String.format("%.2f", realDistance),
                    String.format("%.2f", visualSize),
                    cameraPos,
                    DEBUG_REAL_POSITION
            );
        }

        drawCube(matrixStack, consumer, cameraPos, renderedPos, visualSize);
    }

    private static Vec3 computeRenderedPosition(Vec3 cameraPos, Vec3 realWorldPos) {
        Vec3 dir = realWorldPos.subtract(cameraPos);

        if (dir.lengthSqr() < 1.0E-6) {
            return cameraPos.add(0.0, 0.0, RENDER_DISTANCE_FROM_CAMERA);
        }

        return cameraPos.add(dir.normalize().scale(RENDER_DISTANCE_FROM_CAMERA));
    }

    private static double computeVisualSize(Vec3 cameraPos, Vec3 realWorldPos, double realRadius) {
        double realDistance = cameraPos.distanceTo(realWorldPos);

        if (realDistance < 1.0E-6D) {
            return 100000.0D;
        }

        double angularFactor = realRadius / realDistance;
        double visualSize = angularFactor * RENDER_DISTANCE_FROM_CAMERA * 2.0D;

        if (!Double.isFinite(visualSize)) {
            return 100000.0D;
        }

        return Math.max(MIN_VISUAL_SIZE, visualSize);
    }

    private static void drawCube(
            MatrixStack matrixStack,
            VertexConsumer consumer,
            Vec3 cameraPos,
            Vec3 renderedPos,
            float size
    ) {
        PoseStack poseStack = matrixStack.toPoseStack();
        poseStack.pushPose();

        poseStack.translate(
                renderedPos.x - cameraPos.x,
                renderedPos.y - cameraPos.y,
                renderedPos.z - cameraPos.z
        );

        Matrix4f matrix = poseStack.last().pose();
        float h = size * 0.5f;

        Vector4f frontColor  = new Vector4f(0.30f, 0.65f, 1.00f, 1.0f);
        Vector4f backColor   = new Vector4f(0.20f, 0.40f, 0.85f, 1.0f);
        Vector4f leftColor   = new Vector4f(0.25f, 0.55f, 0.95f, 1.0f);
        Vector4f rightColor  = new Vector4f(0.45f, 0.80f, 1.00f, 1.0f);
        Vector4f topColor    = new Vector4f(0.75f, 0.90f, 1.00f, 1.0f);
        Vector4f bottomColor = new Vector4f(0.15f, 0.25f, 0.45f, 1.0f);

        // Front (+Z)
        quad(consumer, matrix,
                -h, -h,  h,
                -h,  h,  h,
                h,  h,  h,
                h, -h,  h,
                frontColor);

        // Back (-Z)
        quad(consumer, matrix,
                h, -h, -h,
                h,  h, -h,
                -h,  h, -h,
                -h, -h, -h,
                backColor);

        // Left (-X)
        quad(consumer, matrix,
                -h, -h, -h,
                -h,  h, -h,
                -h,  h,  h,
                -h, -h,  h,
                leftColor);

        // Right (+X)
        quad(consumer, matrix,
                h, -h,  h,
                h,  h,  h,
                h,  h, -h,
                h, -h, -h,
                rightColor);

        // Top (+Y)
        quad(consumer, matrix,
                -h,  h,  h,
                -h,  h, -h,
                h,  h, -h,
                h,  h,  h,
                topColor);

        // Bottom (-Y)
        quad(consumer, matrix,
                -h, -h, -h,
                -h, -h,  h,
                h, -h,  h,
                h, -h, -h,
                bottomColor);

        poseStack.popPose();
    }

    private static void quad(
            VertexConsumer consumer,
            Matrix4f matrix,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4,
            Vector4f color
    ) {
        consumer.addVertex(matrix, x1, y1, z1).setColor(color.x, color.y, color.z, color.w);
        consumer.addVertex(matrix, x2, y2, z2).setColor(color.x, color.y, color.z, color.w);
        consumer.addVertex(matrix, x3, y3, z3).setColor(color.x, color.y, color.z, color.w);
        consumer.addVertex(matrix, x4, y4, z4).setColor(color.x, color.y, color.z, color.w);
    }
}