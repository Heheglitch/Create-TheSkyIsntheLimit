package net.ctslteam.ctsl.collision;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.ctslteam.ctsl.registry.CelestialRegistries;
import net.ctslteam.ctsl.util.SubLevelTeleportService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.joml.Vector3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID)
public final class CelestialCollisionHandler {

    private static final long COLLISION_COOLDOWN_TICKS = 40L;
    private static final Map<UUID, Long> PLAYER_COOLDOWNS = new ConcurrentHashMap<>();

    private CelestialCollisionHandler() {
    }

    /**
     * This use the level tick to detect collision with celestials and teleport sublevel
     * @param event
     */
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        Registry<CelestialDefinition> registry =
                level.registryAccess().registryOrThrow(CelestialRegistries.CELESTIAL_REGISTRY_KEY);

        long gameTime = level.getGameTime();

        for (ServerPlayer player : java.util.List.copyOf(level.players())) {
            if (SubLevelTeleportService.isPlayerBusy(player)) {
                continue;
            }

            long nextAllowedTick = PLAYER_COOLDOWNS.getOrDefault(player.getUUID(), 0L);
            if (gameTime < nextAllowedTick) {
                continue;
            }

            for (CelestialDefinition celestial : registry) {
                ResourceLocation linkedDimensionId = celestial.linkedDimension().orElse(null);
                if (linkedDimensionId == null) {
                    continue;
                }

                Vec3 center = new Vec3(
                        celestial.worldAnchor().x(),
                        celestial.worldAnchor().y(),
                        celestial.worldAnchor().z()
                );

                double radius = celestial.collisionRadius();
                double radiusSqr = radius * radius;

                if (player.distanceToSqr(center) > radiusSqr) {
                    continue;
                }

                ServerLevel targetDimension = level.getServer().getLevel(
                        ResourceKey.create(Registries.DIMENSION, linkedDimensionId)
                );
                if (targetDimension == null) {
                    CreateTheSkyIsnttheLimit.LOGGER.warn(
                            "Celestial {} points to missing dimension {}",
                            celestial.id(),
                            linkedDimensionId
                    );
                    continue;
                }

                var tracked = Sable.HELPER.getTrackingSubLevel(player);
                if (!(tracked instanceof ServerSubLevel subLevel)) {
                    continue;
                }

                boolean started = SubLevelTeleportService.warpAndLock(
                        subLevel,
                        targetDimension,
                        new Vector3d(0.0, 100.0, 0.0)
                );

                if (started) {
                    PLAYER_COOLDOWNS.put(player.getUUID(), gameTime + COLLISION_COOLDOWN_TICKS);

                    CreateTheSkyIsnttheLimit.LOGGER.info(
                            "Player {} entered celestial {} -> {}",
                            player.getGameProfile().getName(),
                            celestial.id(),
                            linkedDimensionId
                    );
                } else {
                    PLAYER_COOLDOWNS.put(player.getUUID(), gameTime + 10L);
                }

                break;
            }
        }

        PLAYER_COOLDOWNS.entrySet().removeIf(entry -> gameTime >= entry.getValue());
    }
}