package net.ctslteam.ctsl.render;

import net.minecraft.world.phys.Vec3;

public final class CelestialMath {
    private static final double RENDER_DISTANCE = 10000.0;

    private CelestialMath() {
    }

    public static Vec3 computeApparentPosition(Vec3 cameraPos, Vec3 worldPos) {
        Vec3 dir = worldPos.subtract(cameraPos);
        if (dir.lengthSqr() < 1.0E-6) {
            return cameraPos.add(0.0, 0.0, RENDER_DISTANCE);
        }
        return cameraPos.add(dir.normalize().scale(RENDER_DISTANCE));
    }
}
