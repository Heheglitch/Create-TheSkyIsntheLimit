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
    private static final double MIN_VISUAL_SIZE = 4.0D;
    private static final double MAX_VISUAL_SIZE = 40.0D;

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

        Vec3 apparentPos = computeRenderedPosition(cameraPos, DEBUG_REAL_POSITION);
        float visualSize = (float) computeVisualSize(cameraPos, DEBUG_REAL_POSITION, DEBUG_RADIUS);
        Vector4f color = new Vector4f(0.45f, 0.7f, 1.0f, 1.0f);

        if (mc.level.getGameTime() % 40 == 0) {
            double realDistance = cameraPos.distanceTo(DEBUG_REAL_POSITION);
            CreateTheSkyIsnttheLimit.LOGGER.info(
                    "Debug celestial realDistance={} visualSize={} cameraPos={} realPos={}",
                    String.format("%.2f", realDistance),
                    String.format("%.2f", visualSize),
                    cameraPos,
                    DEBUG_REAL_POSITION
            );
        }

        drawBillboardQuad(matrixStack, consumer, cameraPos, apparentPos, visualSize, color, camera);
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

        if (realDistance < 1.0D) {
            return MAX_VISUAL_SIZE;
        }

        double angularFactor = realRadius / realDistance;
        double visualSize = angularFactor * RENDER_DISTANCE_FROM_CAMERA * 2.0D;

        return clamp(visualSize, MIN_VISUAL_SIZE, MAX_VISUAL_SIZE);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static void drawBillboardQuad(
            MatrixStack matrixStack,
            VertexConsumer consumer,
            Vec3 cameraPos,
            Vec3 apparentPos,
            float size,
            Vector4f color,
            Camera camera
    ) {
        PoseStack poseStack = matrixStack.toPoseStack();
        poseStack.pushPose();

        poseStack.translate(
                apparentPos.x - cameraPos.x,
                apparentPos.y - cameraPos.y,
                apparentPos.z - cameraPos.z
        );

        poseStack.mulPose(camera.rotation());

        Matrix4f matrix = poseStack.last().pose();
        float half = size * 0.5f;

        consumer.addVertex(matrix, -half, -half, 0.0f).setColor(color.x, color.y, color.z, color.w);
        consumer.addVertex(matrix, -half,  half, 0.0f).setColor(color.x, color.y, color.z, color.w);
        consumer.addVertex(matrix,  half,  half, 0.0f).setColor(color.x, color.y, color.z, color.w);
        consumer.addVertex(matrix,  half, -half, 0.0f).setColor(color.x, color.y, color.z, color.w);

        poseStack.popPose();
    }
}