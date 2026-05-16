package net.ctslteam.ctsl.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.platform.VeilEventPlatform;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public final class CelestialVeilRendering {
    public static final RenderType CELESTIAL_RENDER_TYPE = RenderType.create(
            "create_theskyisnthelimit:celestials",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderType.TRANSLUCENT_TARGET)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setCullState(RenderType.NO_CULL)
                    .setWriteMaskState(RenderType.COLOR_WRITE)
                    .createCompositeState(false)
    );

    private CelestialVeilRendering() {
    }

    public static void init() {
        VeilEventPlatform.INSTANCE.onVeilRenderLevelStage((
                stage,
                levelRenderer,
                bufferSource,
                matrixStack,
                projectionMatrix,
                renderTickMatrix,
                renderTick,
                deltaTracker,
                camera,
                frustum
        ) -> {
            if (stage != VeilRenderLevelStageEvent.Stage.AFTER_SKY) {
                return;
            }

            CelestialVeilRenderer.render(bufferSource, matrixStack, deltaTracker, camera);
        });
    }
}
