package net.ctslteam.ctsl.registry;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * This is the class that contain registry key of celestial
 * You can add celestial in your mods/datapacks in data/ctsl/celestials
 */
public final class CelestialRegistries {
    public static final ResourceKey<Registry<CelestialDefinition>> CELESTIAL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(CreateTheSkyIsnttheLimit.MOD_ID, "celestials")
            );

    private CelestialRegistries() {
    }
}