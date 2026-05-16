package net.ctslteam.ctsl.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.ctslteam.ctsl.runtime.CelestialDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class CelestialDataLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();
    private static final String FOLDER = "celestials";

    public CelestialDataLoader() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, CelestialDefinition> loaded = new LinkedHashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation fileId = entry.getKey();
            JsonElement json = entry.getValue();

            CelestialDefinition.CODEC.parse(JsonOps.INSTANCE, json)
                    .resultOrPartial(error -> LOGGER.error("Failed to parse celestial {}: {}", fileId, error))
                    .ifPresent(definition -> {
                        validate(fileId, definition);
                        if (loaded.put(definition.id(), definition) != null) {
                            LOGGER.warn("Duplicate celestial id '{}' from {}", definition.id(), fileId);
                        }
                    });
        }

        CelestialDataManager.replaceAll(loaded);
        LOGGER.info("Loaded {} celestial definitions for {}", loaded.size(), CreateTheSkyIsnttheLimit.MOD_ID);
    }

    private static void validate(ResourceLocation fileId, CelestialDefinition definition) {
        if ((definition.type().name().equals("PLANET") || definition.type().name().equals("MOON"))
                && definition.linkedDimension().isEmpty()) {
            LOGGER.warn("Celestial {} in {} should define linked_dimension because it is {}", definition.id(), fileId, definition.type());
        }

        if (definition.type().name().equals("STAR") && definition.linkedDimension().isPresent()) {
            LOGGER.warn("Celestial {} in {} is a STAR but has linked_dimension={}", definition.id(), fileId, definition.linkedDimension().get());
        }
    }
}
