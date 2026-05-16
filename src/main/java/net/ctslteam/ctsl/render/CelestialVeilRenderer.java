package net.ctslteam.ctsl.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import foundry.veil.api.client.render.MatrixStack;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimitClient;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class CelestialVeilRenderer {
    private CelestialVeilRenderer() {
    }

    public static void render(
            MultiBufferSource bufferSource,
            MatrixStack matrixStack,
            DeltaTracker deltaTracker,
            Camera camera
    ) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }

        if (mc.level.getGameTime() % 20 == 0) {
            CreateTheSkyIsnttheLimit.LOGGER.info("[CTSL] CelestialVeilRenderer.render() called");
        }

        Vec3 cameraPos = camera.getPosition();
        VertexConsumer consumer = bufferSource.getBuffer(CelestialRenderTypes.CELESTIALS);

        Vector3f look = camera.getLookVector();
        Vec3 forward = new Vec3(look.x(), look.y(), look.z());
        Vec3 apparent = cameraPos.add(forward.scale(100.0));

        float size = 30.0f;
        Vector4f color = new Vector4f(1f, 0f, 0f, 1f);

        drawBillboardQuad(matrixStack, consumer, cameraPos, apparent, size, color, camera);
    }

    private static void drawBillboardQuad(MatrixStack matrixStack,
                                          VertexConsumer consumer,
                                          Vec3 cameraPos,
                                          Vec3 apparentPos,
                                          float size,
                                          Vector4f color,
                                          Camera camera) {
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