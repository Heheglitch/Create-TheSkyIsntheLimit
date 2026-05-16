package net.ctslteam.ctsl.event;

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
import net.ctslteam.ctsl.config.CtslServer;
import net.ctslteam.ctsl.dimension.CtslDimensions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Vector3d;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID)
public final class CtslCommonEvents {

    private static final Map<UUID, PendingLock> PENDING_LOCKS = new ConcurrentHashMap<>();
    private static final Map<UUID, ActiveLock> ACTIVE_LOCKS = new ConcurrentHashMap<>();

    private record PendingLock(
            ResourceKey<Level> targetDimension,
            Vector3d targetPos,
            String expectedName,
            int tries
    ) {
        PendingLock nextTry() {
            return new PendingLock(targetDimension, targetPos, expectedName, tries + 1);
        }
    }

    private record ActiveLock(
            ResourceKey<Level> dimension,
            UUID subLevelId,
            FixedConstraintHandle handle,
            long unlockGameTime
    ) {}

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide()) {
            return;
        }

        if (!level.dimension().location().equals(ResourceLocation.fromNamespaceAndPath("ctsl", "space"))) {
            return;
        }

        DamageSource source = event.getSource();
        DamageSources sources = entity.damageSources();

        if (source == sources.fellOutOfWorld() || source == sources.fall()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.tickCount % 10 != 0) return;

        handlePendingLocks(player);
        handleActiveLocks(player);

        var tracked = Sable.HELPER.getTrackingSubLevel(player);
        if (!(tracked instanceof ServerSubLevel subLevel)) return;

        double subLevelHeight = subLevel.logicalPose().position().y;
        if (subLevelHeight < CtslServer.SPACE_HEIGHT.get()) return;
        if (CtslDimensions.isSpace(player.level())) return;

        UUID oldSubLevelId = subLevel.getUniqueId();
        if (PENDING_LOCKS.containsKey(oldSubLevelId)) return;

        ServerLevel space = player.server.getLevel(
                ResourceKey.create(
                        Registries.DIMENSION,
                        ResourceLocation.fromNamespaceAndPath("ctsl", "space")
                )
        );
        if (space == null) return;

        Vector3d targetPos = new Vector3d(0, 10, 0);

        CreateTheSkyIsnttheLimit.LOGGER.info(
                "Player : {} is in space with this sub level : {}",
                player.getName().getString(),
                subLevel.getName()
        );

        PENDING_LOCKS.put(
                oldSubLevelId,
                new PendingLock(
                        space.dimension(),
                        new Vector3d(targetPos),
                        subLevel.getName(),
                        0
                )
        );

        SubLevelWarper.WarpSubLevel(subLevel, space, targetPos);
    }

    private static void handlePendingLocks(ServerPlayer player) {
        Iterator<Map.Entry<UUID, PendingLock>> it = PENDING_LOCKS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, PendingLock> entry = it.next();
            PendingLock pending = entry.getValue();

            ServerLevel targetLevel = player.server.getLevel(pending.targetDimension());
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

    private static void handleActiveLocks(ServerPlayer player) {
        Iterator<Map.Entry<UUID, ActiveLock>> it = ACTIVE_LOCKS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, ActiveLock> entry = it.next();
            ActiveLock active = entry.getValue();

            ServerLevel level = player.server.getLevel(active.dimension());
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
        if (container == null) return null;

        ServerSubLevel best = null;
        double bestDistanceSq = Double.MAX_VALUE;

        for (ServerSubLevel subLevel : container.getAllSubLevels()) {
            if (subLevel == null || subLevel.isRemoved()) continue;

            if (pending.expectedName() != null && subLevel.getName() != null) {
                if (!pending.expectedName().equals(subLevel.getName())) continue;
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