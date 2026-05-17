package net.ctslteam.ctsl.registry;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class CelestialRegistries {
    public static final ResourceKey<Registry<CelestialDefinition>> CELESTIAL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(CreateTheSkyIsnttheLimit.MOD_ID, "celestials")
            );

    private CelestialRegistries() {
    }
}