package net.ctslteam.ctsl.api.celestials;

import net.minecraft.world.phys.Vec3;

public record CelestialRuntimeState(
        CelestialDefinition definition,
        Vec3 currentPosition
) {}