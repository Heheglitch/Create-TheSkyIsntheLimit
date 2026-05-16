package net.ctslteam.ctsl.runtime;

import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.ctslteam.ctsl.api.celestials.CelestialRuntimeState;
import net.ctslteam.ctsl.api.celestials.OrbitDefinition;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public final class CelestialManager {

    private final Map<String, CelestialDefinition> definitions = new HashMap<>();

    public void replaceAll(Collection<CelestialDefinition> defs) {
        definitions.clear();
        for (CelestialDefinition def : defs) {
            definitions.put(def.id(), def);
        }
    }

    public Collection<CelestialRuntimeState> computeStates(long gameTime) {
        List<CelestialRuntimeState> result = new ArrayList<>();
        for (CelestialDefinition def : definitions.values()) {
            result.add(new CelestialRuntimeState(def, computePosition(def, gameTime)));
        }
        return result;
    }

    public Vec3 computePosition(CelestialDefinition def, long gameTime) {
        if (def.orbit().isEmpty()) {
            return def.worldAnchor();
        }

        OrbitDefinition orbit = def.orbit().get();
        double angle = ((gameTime % orbit.periodTicks()) / (double) orbit.periodTicks()) * (Math.PI * 2.0);
        angle += Math.toRadians(orbit.phaseDeg());

        double x = Math.cos(angle) * orbit.semiMajorAxis();
        double z = Math.sin(angle) * orbit.semiMajorAxis();
        double y = Math.sin(Math.toRadians(orbit.inclinationDeg())) * z;

        return def.worldAnchor().add(x, y, z);
    }
}
