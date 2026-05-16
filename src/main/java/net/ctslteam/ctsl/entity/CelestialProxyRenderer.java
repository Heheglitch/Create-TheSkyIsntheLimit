package net.ctslteam.ctsl.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CelestialProxyRenderer extends EntityRenderer<CelestialProxyEntity> {

    public CelestialProxyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CelestialProxyEntity entity) {
        return ResourceLocation.withDefaultNamespace("textures/block/glass.png");
    }
}