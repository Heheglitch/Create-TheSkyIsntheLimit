package net.ctslteam.ctsl.runtime;

import net.ctslteam.ctsl.api.celestials.CelestialDefinition;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class CelestialDataManager {
    private static final Map<String, CelestialDefinition> CELESTIALS = new LinkedHashMap<>();

    private CelestialDataManager() {
    }

    public static void replaceAll(Map<String, CelestialDefinition> newValues) {
        CELESTIALS.clear();
        CELESTIALS.putAll(newValues);
    }

    public static Collection<CelestialDefinition> values() {
        return CELESTIALS.values();
    }

    public static Optional<CelestialDefinition> get(String id) {
        return Optional.ofNullable(CELESTIALS.get(id));
    }

    public static Map<String, CelestialDefinition> asMap() {
        return Map.copyOf(CELESTIALS);
    }

    public static void clear() {
        CELESTIALS.clear();
    }
}
