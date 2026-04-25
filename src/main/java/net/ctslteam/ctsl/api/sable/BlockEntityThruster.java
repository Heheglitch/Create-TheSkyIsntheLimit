package net.ctslteam.ctsl.api.sable;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

/**
 * Spinny spin spin, woosh woosh!
 */
public interface BlockEntityThruster {

    /**
     * @return the direction of the thruster
     */
    Direction getBlockDirection();

    /**
     * @return thrust in [pN]
     */
    double getThrust();

    default double getScaledThrust() {
        return -this.getThrust();
    }

    /**
     * @return if the thruster is active / thrust should be computed
     */
    boolean isActive();

    Level getLevel();

    BlockPos getBlockPos();
}

