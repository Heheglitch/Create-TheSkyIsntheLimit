package net.ctslteam.ctsl.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tterrag.registrate.builders.FluidBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CtslFluidType extends FluidType {
    private Vector3f fogColor;
    private Supplier<Float> fogDistance;
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;


    public CtslFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
    }

    public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance, Factory factory) {
        return (p, s, f) -> {
            ResourceLocation still = ResourceLocation.fromNamespaceAndPath(s.getNamespace(), s.getPath().replace("fluid/", "block/"));
            ResourceLocation flow = ResourceLocation.fromNamespaceAndPath(f.getNamespace(), f.getPath().replace("fluid/", "block/"));

            CtslFluidType fluidType = factory.create(p, still, flow);
            fluidType.fogColor = new Color(fogColor, false).asVectorF();
            fluidType.fogDistance = fogDistance;
            return fluidType;
        };
    }

    public interface Factory {
            CtslFluidType create(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture);
    }

    /*@Override
    public @NotNull ResourceLocation getStillTexture() {
        return this.stillTexture;
    }

    @Override
    public @NotNull ResourceLocation getFlowingTexture() {
        return this.flowingTexture;
    }

    @Override
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        Vector3f customFogColor = this.getCustomFogColor();
        return customFogColor == null ? fluidFogColor : customFogColor;
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        IClientFluidTypeExtensions.super.modifyFogRender(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape);
        float modifier = this.getFogDistanceModifier();
        float baseWaterFog = 96.0f;
        if (modifier != 1.0f) {
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
            RenderSystem.setShaderFogStart(-8);
            RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
        }
    }

     */

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return stillTexture;
                //return ResourceLocation.fromNamespaceAndPath("ctsl", "block/hydrogen_fluid_still");
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return flowingTexture;
                //return ResourceLocation.fromNamespaceAndPath("ctsl", "block/hydrogen_fluid_flow");
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                Vector3f customFogColor = getCustomFogColor();
                return customFogColor == null ? fluidFogColor : customFogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                float modifier = getFogDistanceModifier();
                float baseWaterFog = 96.0f;
                if (modifier != 1.0f) {
                    RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                    RenderSystem.setShaderFogStart(-8);
                    RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                }
            }
        });
    }

    public Vector3f getCustomFogColor() {
        return this.fogColor;
    }

    public float getFogDistanceModifier() {
        return this.fogDistance.get();
    }
}
