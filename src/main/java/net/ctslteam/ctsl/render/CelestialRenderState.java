package net.ctslteam.ctsl.render;

import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.minecraft.world.phys.Vec3;

public record CelestialRenderState(
        CelestialDefinition definition,
        Vec3 apparentPosition,
        float size
) {
}
