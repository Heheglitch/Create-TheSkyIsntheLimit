package net.ctslteam.ctsl.util;

import dev.egg.SubLevelWarper;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.constraint.fixed.FixedConstraintConfiguration;
import dev.ryanhcode.sable.api.physics.constraint.fixed.FixedConstraintHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to handle the teleportation of a {@link ServerSubLevel}
 */
public final class SubLevelTeleportService {

    private static final int MAX_PENDING_TRIES = 40;
    private static final long LOCK_DURATION_TICKS = 100L;
    private static final double MATCH_DISTANCE_SQ = 64.0D;

    private static final Map<UUID, PendingLock> PENDING_LOCKS = new ConcurrentHashMap<>();
    private static final Map<UUID, ActiveLock> ACTIVE_LOCKS = new ConcurrentHashMap<>();

    /**
     * This record is used to check what {@link ServerSubLevel} need to be locked
     * @param sourceSubLevelId
     * @param targetDimension
     * @param targetPos
     * @param expectedName
     * @param tries
     */
    private record PendingLock(
            UUID sourceSubLevelId,
            ResourceKey<Level> targetDimension,
            Vector3d targetPos,
            String expectedName,
            int tries
    ) {
        private PendingLock nextTry() {
            return new PendingLock(sourceSubLevelId, targetDimension, targetPos, expectedName, tries + 1);
        }
    }

    private record ActiveLock(
            ResourceKey<Level> dimension,
            UUID subLevelId,
            FixedConstraintHandle handle,
            long unlockGameTime
    ) {
    }

    private SubLevelTeleportService() {
    }

    public static boolean isPlayerBusy(ServerPlayer player) {
        if (player == null) {
            return false;
        }

        var tracked = Sable.HELPER.getTrackingSubLevel(player);
        if (!(tracked instanceof ServerSubLevel subLevel)) {
            return false;
        }

        UUID id = subLevel.getUniqueId();
        return PENDING_LOCKS.containsKey(id) || ACTIVE_LOCKS.containsKey(id);
    }

    /**
     * This function will Warp/Teleport {@link ServerPlayer} and add his tracked {@link ServerSubLevel} to the {@link PendingLock}
     * @param serverPlayer
     * @param targetLevel
     * @param targetPos
     * @return True or False depending on the success
     */
    public static boolean warpAndLock(ServerPlayer serverPlayer, ServerLevel targetLevel, Vector3d targetPos) {
        if (serverPlayer == null || targetLevel == null || targetPos == null) {
            return false;
        }

        var tracked = Sable.HELPER.getTrackingSubLevel(serverPlayer);
        if (!(tracked instanceof ServerSubLevel subLevel)) {
            CreateTheSkyIsnttheLimit.LOGGER.warn(
                    "Cannot warp player {}: no tracked server sublevel",
                    serverPlayer.getGameProfile().getName()
            );
            return false;
        }

        return warpAndLock(subLevel, targetLevel, targetPos);
    }

    /**
     * This function will Warp/Teleport {@link ServerSubLevel} and add it to the {@link PendingLock}
     * @param subLevel
     * @param targetLevel
     * @param targetPos
     * @return True or False depending on the success
     */
    public static boolean warpAndLock(ServerSubLevel subLevel, ServerLevel targetLevel, Vector3d targetPos) {
        if (subLevel == null || targetLevel == null || targetPos == null) {
            return false;
        }

        UUID sourceSubLevelId = subLevel.getUniqueId();
        if (PENDING_LOCKS.containsKey(sourceSubLevelId) || ACTIVE_LOCKS.containsKey(sourceSubLevelId)) {
            return false;
        }

        PendingLock pending = new PendingLock(
                sourceSubLevelId,
                targetLevel.dimension(),
                new Vector3d(targetPos),
                subLevel.getName(),
                0
        );

        PENDING_LOCKS.put(sourceSubLevelId, pending);

        SubLevelWarper.WarpSubLevel(subLevel, targetLevel, targetPos);

        CreateTheSkyIsnttheLimit.LOGGER.info(
                "Warping sublevel {} ({}) to {} at [{}, {}, {}]",
                subLevel.getName(),
                sourceSubLevelId,
                targetLevel.dimension().location(),
                targetPos.x,
                targetPos.y,
                targetPos.z
        );

        return true;
    }

    /**
     * This is the function that need to be run on the main server tick
     * @param server
     */
    public static void tick(MinecraftServer server) {
        if (server == null) {
            return;
        }

        handlePendingLocks(server);
        handleActiveLocks(server);
    }

    /**
     * This function handle the {@link PendingLock}
     * @param server
     */
    private static void handlePendingLocks(MinecraftServer server) {
        for (Map.Entry<UUID, PendingLock> entry : PENDING_LOCKS.entrySet()) {
            UUID pendingId = entry.getKey();
            PendingLock pending = entry.getValue();

            ServerLevel targetLevel = server.getLevel(pending.targetDimension());
            if (targetLevel == null) {
                PENDING_LOCKS.remove(pendingId, pending);
                continue;
            }

            ServerSubLevel copy = findWarpedSubLevel(targetLevel, pending);
            if (copy == null) {
                if (pending.tries() >= MAX_PENDING_TRIES) {
                    CreateTheSkyIsnttheLimit.LOGGER.warn(
                            "Unable to find warped sublevel from {} in {} after {} tries",
                            pending.sourceSubLevelId(),
                            pending.targetDimension().location(),
                            pending.tries()
                    );
                    PENDING_LOCKS.remove(pendingId, pending);
                } else {
                    PENDING_LOCKS.replace(pendingId, pending, pending.nextTry());
                }
                continue;
            }

            if (ACTIVE_LOCKS.containsKey(copy.getUniqueId())) {
                PENDING_LOCKS.remove(pendingId, pending);
                continue;
            }

            SubLevelPhysicsSystem physics = SubLevelPhysicsSystem.get(targetLevel);
            if (physics == null) {
                CreateTheSkyIsnttheLimit.LOGGER.warn(
                        "Missing physics system in target dimension {} for warped sublevel {}",
                        targetLevel.dimension().location(),
                        copy.getUniqueId()
                );
                PENDING_LOCKS.remove(pendingId, pending);
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
                            targetLevel.getGameTime() + LOCK_DURATION_TICKS
                    )
            );

            CreateTheSkyIsnttheLimit.LOGGER.info(
                    "Locked warped sublevel {} ({}) in {} for {} ticks",
                    copy.getName(),
                    copy.getUniqueId(),
                    targetLevel.dimension().location(),
                    LOCK_DURATION_TICKS
            );

            PENDING_LOCKS.remove(pendingId, pending);
        }
    }

    /**
     * This function handle the {@link ActiveLock}
     * @param server
     */
    private static void handleActiveLocks(MinecraftServer server) {
        for (Map.Entry<UUID, ActiveLock> entry : ACTIVE_LOCKS.entrySet()) {
            UUID subLevelId = entry.getKey();
            ActiveLock active = entry.getValue();

            ServerLevel level = server.getLevel(active.dimension());
            if (level == null) {
                ACTIVE_LOCKS.remove(subLevelId, active);
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

            ACTIVE_LOCKS.remove(subLevelId, active);
        }
    }

    /**
     * This function is used to find the warped/teleported {@link ServerSubLevel}
     * @param level
     * @param pending
     * @return {@link ServerSubLevel}
     */
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

            if (ACTIVE_LOCKS.containsKey(subLevel.getUniqueId())) {
                continue;
            }

            String expectedName = pending.expectedName();
            String actualName = subLevel.getName();
            if (expectedName != null && actualName != null && !expectedName.equals(actualName)) {
                continue;
            }

            double distSq = subLevel.logicalPose().position().distanceSquared(pending.targetPos());
            if (distSq < bestDistanceSq) {
                bestDistanceSq = distSq;
                best = subLevel;
            }
        }

        return bestDistanceSq <= MATCH_DISTANCE_SQ ? best : null;
    }
}