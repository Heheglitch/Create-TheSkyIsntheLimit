package net.ctslteam.ctsl.data;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID, value = Dist.CLIENT)
public final class CelestialReloadListener {
    private CelestialReloadListener() {
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new CelestialDataLoader());
    }
}