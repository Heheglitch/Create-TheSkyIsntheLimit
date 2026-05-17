package net.ctslteam.ctsl.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public final class CelestialRenderTypes {
    public static final RenderType CELESTIALS = RenderType.create(
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
                    .setCullState(RenderType.CULL)
                    .setWriteMaskState(RenderType.COLOR_WRITE)
                    .createCompositeState(false)
    );

    private CelestialRenderTypes() {
    }
}
