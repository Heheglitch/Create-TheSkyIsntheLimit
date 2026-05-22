package net.ctslteam.ctsl.util;

import dev.egg.SubLevelWarper;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.constraint.fixed.FixedConstraintConfiguration;
import dev.ryanhcode.sable.api.physics.constraint.fixed.FixedConstraintHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SubLevelTeleportService {

    private static final Map<UUID, PendingLock> PENDING_LOCKS = new ConcurrentHashMap<>();
    private static final Map<UUID, ActiveLock> ACTIVE_LOCKS = new ConcurrentHashMap<>();

    private record PendingLock(
            ResourceKey<Level> targetDimension,
            Vector3d targetPos,
            String expectedName,
            int tries
    ) {
        private PendingLock nextTry() {
            return new PendingLock(targetDimension, targetPos, expectedName, tries + 1);
        }
    }

    private record ActiveLock(
            ResourceKey<Level> dimension,
            UUID subLevelId,
            FixedConstraintHandle handle,
            long unlockGameTime
    ) {}

    private SubLevelTeleportService() {
    }

    public static boolean warpAndLock(ServerPlayer serverPlayer, ServerLevel targetLevel, Vector3d targetPos) {
        if (serverPlayer == null || targetLevel == null || targetPos == null) {
            return false;
        }

        var tracked = Sable.HELPER.getTrackingSubLevel(serverPlayer);
        if (!(tracked instanceof ServerSubLevel subLevel)) return false;

        warpAndLock(subLevel, targetLevel, targetPos);
        return true;
    }

    public static boolean warpAndLock(ServerSubLevel subLevel, ServerLevel targetLevel, Vector3d targetPos) {
        if (subLevel == null || targetLevel == null || targetPos == null) {
            return false;
        }

        UUID oldSubLevelId = subLevel.getUniqueId();
        if (PENDING_LOCKS.containsKey(oldSubLevelId) || ACTIVE_LOCKS.containsKey(oldSubLevelId)) {
            return false;
        }

        PENDING_LOCKS.put(
                oldSubLevelId,
                new PendingLock(
                        targetLevel.dimension(),
                        new Vector3d(targetPos),
                        subLevel.getName(),
                        0
                )
        );

        SubLevelWarper.WarpSubLevel(subLevel, targetLevel, targetPos);

        CreateTheSkyIsnttheLimit.LOGGER.info(
                "Warping sublevel {} to {} at {} {} {}",
                subLevel.getName(),
                targetLevel.dimension().location(),
                targetPos.x,
                targetPos.y,
                targetPos.z
        );

        return true;
    }

    public static void tick(MinecraftServer server) {
        handlePendingLocks(server);
        handleActiveLocks(server);
    }

    private static void handlePendingLocks(MinecraftServer server) {
        Iterator<Map.Entry<UUID, PendingLock>> it = PENDING_LOCKS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, PendingLock> entry = it.next();
            PendingLock pending = entry.getValue();

            ServerLevel targetLevel = server.getLevel(pending.targetDimension());
            if (targetLevel == null) {
                it.remove();
                continue;
            }

            ServerSubLevel copy = findWarpedSubLevel(targetLevel, pending);
            if (copy == null) {
                if (pending.tries() > 40) {
                    CreateTheSkyIsnttheLimit.LOGGER.warn(
                            "Unable to find warped sublevel in {} after {} tries",
                            pending.targetDimension().location(),
                            pending.tries()
                    );
                    it.remove();
                } else {
                    entry.setValue(pending.nextTry());
                }
                continue;
            }

            if (ACTIVE_LOCKS.containsKey(copy.getUniqueId())) {
                it.remove();
                continue;
            }

            SubLevelPhysicsSystem physics = SubLevelPhysicsSystem.get(targetLevel);
            if (physics == null) {
                it.remove();
                continue;
            }

            PhysicsPipeline pipeline = physics.getPipeline();
            pipeline.resetVelocity(copy);

            FixedConstraintHandle handle = pipeline.addConstraint(
                    null,
                    copy,
                    new FixedConstraintConfiguration(
                            copy.logicalPose().position(),
                            copy.logicalPose().rotationPoint(),
                            copy.logicalPose().orientation()
                    )
            );

            ACTIVE_LOCKS.put(
                    copy.getUniqueId(),
                    new ActiveLock(
                            targetLevel.dimension(),
                            copy.getUniqueId(),
                            handle,
                            targetLevel.getGameTime() + 100L
                    )
            );

            CreateTheSkyIsnttheLimit.LOGGER.info(
                    "Locked warped sublevel {} in dimension {} for 5 seconds",
                    copy.getName(),
                    targetLevel.dimension().location()
            );

            it.remove();
        }
    }

    private static void handleActiveLocks(MinecraftServer server) {
        Iterator<Map.Entry<UUID, ActiveLock>> it = ACTIVE_LOCKS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, ActiveLock> entry = it.next();
            ActiveLock active = entry.getValue();

            ServerLevel level = server.getLevel(active.dimension());
            if (level == null) {
                it.remove();
                continue;
            }

            if (level.getGameTime() < active.unlockGameTime()) {
                continue;
            }

            FixedConstraintHandle handle = active.handle();
            if (handle != null && handle.isValid()) {
                handle.remove();
                CreateTheSkyIsnttheLimit.LOGGER.info(
                        "Unlocked sublevel {} in dimension {}",
                        active.subLevelId(),
                        active.dimension().location()
                );
            }

            it.remove();
        }
    }

    private static ServerSubLevel findWarpedSubLevel(ServerLevel level, PendingLock pending) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return null;
        }

        ServerSubLevel best = null;
        double bestDistanceSq = Double.MAX_VALUE;

        for (ServerSubLevel subLevel : container.getAllSubLevels()) {
            if (subLevel == null || subLevel.isRemoved()) {
                continue;
            }

            if (pending.expectedName() != null && subLevel.getName() != null
                    && !pending.expectedName().equals(subLevel.getName())) {
                continue;
            }

            double distSq = subLevel.logicalPose().position().distanceSquared(pending.targetPos());
            if (distSq < bestDistanceSq) {
                bestDistanceSq = distSq;
                best = subLevel;
            }
        }

        return bestDistanceSq <= 64.0D ? best : null;
    }
}