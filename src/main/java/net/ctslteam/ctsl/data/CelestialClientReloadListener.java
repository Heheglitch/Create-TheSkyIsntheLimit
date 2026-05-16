package net.ctslteam.ctsl.data;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

public final class CelestialClientReloadListener {
    private CelestialClientReloadListener() {
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new CelestialClientDataLoader());
    }
}