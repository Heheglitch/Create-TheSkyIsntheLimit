package net.ctslteam.ctsl.api.celestials;

import net.minecraft.world.phys.Vec3;

/**
 * This is used to calculate at runtime the position of celestials
 * @param definition
 * @param currentPosition
 */
public record CelestialRuntimeState(
        CelestialDefinition definition,
        Vec3 currentPosition
) {}