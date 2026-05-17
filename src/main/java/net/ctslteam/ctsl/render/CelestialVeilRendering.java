package net.ctslteam.ctsl.render;

import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.platform.VeilEventPlatform;

public final class CelestialVeilRendering {
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
