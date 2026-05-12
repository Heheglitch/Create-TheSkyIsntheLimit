package net.ctslteam.ctsl.client.space;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * Custom visual effects for the CTSL space dimension.
 *
 * This class controls:
 * - sky color behavior
 * - fog behavior
 * - general dimension rendering profile
 *
 * The actual celestial objects are rendered by SpaceSkyRenderer.
 */
public class SpaceDimensionSpecialEffects extends DimensionSpecialEffects {

    public static final SpaceDimensionSpecialEffects INSTANCE = new SpaceDimensionSpecialEffects();

    public SpaceDimensionSpecialEffects() {
        super(
                Float.NaN,     // cloud height
                false,         // alternate sky color
                SkyType.NONE,  // pas de skybox vanilla
                false,         // brighten lightmap
                false           // darkened
        );
    }

    @Override
    public @NotNull Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float brightness) {
        return new Vec3(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        return true;
    }

    @Override
    public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {
        return true;
    }
}
