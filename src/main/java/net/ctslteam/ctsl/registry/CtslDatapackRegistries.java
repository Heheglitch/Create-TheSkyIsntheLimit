package net.ctslteam.ctsl.registry;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class CtslDatapackRegistries {
    private CtslDatapackRegistries() {
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                CelestialRegistries.CELESTIAL_REGISTRY_KEY,
                CelestialDefinition.CODEC,
                CelestialDefinition.CODEC
        );
    }
}