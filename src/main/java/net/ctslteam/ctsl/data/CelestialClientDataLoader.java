package net.ctslteam.ctsl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.ctslteam.ctsl.runtime.CelestialClientDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class CelestialClientDataLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();
    private static final String FOLDER = "celestials";

    public CelestialClientDataLoader() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, CelestialDefinition> loaded = new LinkedHashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation fileId = entry.getKey();
            JsonElement json = entry.getValue();

            CelestialDefinition.CODEC.parse(JsonOps.INSTANCE, json)
                    .resultOrPartial(error -> LOGGER.error("Client celestial parse error {}: {}", fileId, error))
                    .ifPresent(def -> loaded.put(def.id(), def));
        }

        CelestialClientDataManager.replaceAll(loaded);
        LOGGER.info("Client loaded {} celestial definitions", loaded.size());
    }
}
