package net.ctslteam.ctsl.api.sable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

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

    /**
     * @return if the thruster is active / thrust should be computed
     */
    boolean isActive();

    Level getLevel();

    BlockPos getBlockPos();
}

