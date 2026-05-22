package net.ctslteam.ctsl.collision;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.api.celestials.CelestialDefinition;
import net.ctslteam.ctsl.registry.CelestialRegistries;
import net.ctslteam.ctsl.util.SubLevelTeleportService;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Registry;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.joml.Vector3d;

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID)
public final class CelestialCollisionHandler {
    private CelestialCollisionHandler() {
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        Registry<CelestialDefinition> registry =
                level.registryAccess().registryOrThrow(CelestialRegistries.CELESTIAL_REGISTRY_KEY);

        for (ServerPlayer player : level.players()) {

            for (CelestialDefinition celestial : registry) {
                Vec3 center = new Vec3(
                        celestial.worldAnchor().x(),
                        celestial.worldAnchor().y(),
                        celestial.worldAnchor().z()
                );

                double radius = celestial.collisionRadius();
                double radiusSqr = radius * radius;

                if (player.distanceToSqr(center) <= radiusSqr) {
                    CreateTheSkyIsnttheLimit.LOGGER.info(player.getDisplayName() + " is entering : " + celestial.id());

                    ServerLevel targetDimension = event.getLevel().getServer().getLevel(
                            ResourceKey.create(
                                    Registries.DIMENSION,
                                    celestial.linkedDimension().get()
                            )
                    );

                    SubLevelTeleportService.warpAndLock(player, targetDimension, new Vector3d(0, 100, 0));
                }
            }
        }
    }
}