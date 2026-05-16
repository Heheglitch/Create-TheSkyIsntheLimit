package net.ctslteam.ctsl.render;

import net.ctslteam.ctsl.api.celestials.CelestialType;
import org.joml.Vector4f;


public final class CelestialVisuals {
    private CelestialVisuals() {
    }

    public static Vector4f colorFor(CelestialType type) {
        return switch (type) {
            case STAR -> new Vector4f(1.0f, 0.95f, 0.7f, 0.95f);
            case PLANET -> new Vector4f(0.6f, 0.8f, 1.0f, 0.9f);
            case MOON -> new Vector4f(0.85f, 0.85f, 0.9f, 0.9f);
        };
    }

    public static float baseSize(CelestialType type) {
        return switch (type) {
            case STAR -> 90.0f;
            case PLANET -> 55.0f;
            case MOON -> 30.0f;
        };
    }
}
