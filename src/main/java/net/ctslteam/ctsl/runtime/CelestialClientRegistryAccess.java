package net.ctslteam.ctsl.runtime;

import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.ctslteam.ctsl.registry.CelestialRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.Collection;
import java.util.List;

public final class CelestialClientRegistryAccess {
    private CelestialClientRegistryAccess() {
    }

    public static Collection<CelestialDefinition> values() {
        Minecraft mc = Minecraft.getInstance();
        ClientPacketListener connection = mc.getConnection();
        if (connection == null) {
            return List.of();
        }

        var lookup = connection.registryAccess().lookup(CelestialRegistries.CELESTIAL_REGISTRY_KEY);
        if (lookup.isEmpty()) {
            return List.of();
        }

        return lookup.get()
                .listElements()
                .map(holder -> holder.value())
                .toList();
    }
}