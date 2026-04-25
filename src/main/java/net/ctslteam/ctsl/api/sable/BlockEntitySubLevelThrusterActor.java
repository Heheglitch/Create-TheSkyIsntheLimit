package net.ctslteam.ctsl.api.sable;

import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public interface BlockEntitySubLevelThrusterActor extends BlockEntitySubLevelActor {

    Vector3d THRUST_VECTOR = new Vector3d();
    Vector3d THRUST_POSITION = new Vector3d();

    BlockEntityThruster getThruster();

    @Override
    default void sable$physicsTick(ServerSubLevel subLevel, RigidBodyHandle handle, double timeStep) {
        final BlockEntityThruster thruster = this.getThruster();

        if (thruster.isActive()) {
            final Vec3 thrustDirection = Vec3.atLowerCornerOf(thruster.getBlockDirection().getNormal());
            this.applyForces(subLevel, thrustDirection, timeStep);
        }
    }

    default void applyForces(final ServerSubLevel subLevel, final Vec3 thrustDirection, final double timeStep) {
        final BlockEntityThruster thruster = this.getThruster();
        final Vec3 thrust = thrustDirection.scale(thruster.getThrust() * timeStep);

        THRUST_POSITION.set(JOMLConversion.atCenterOf(thruster.getBlockPos()));
        THRUST_VECTOR.set(thrust.x, thrust.y, thrust.z);

        final QueuedForceGroup forceGroup = subLevel.getOrCreateQueuedForceGroup(ForceGroups.PROPULSION.get());
        forceGroup.applyAndRecordPointForce(new Vector3d(THRUST_POSITION), new Vector3d(THRUST_VECTOR));
    }
}
