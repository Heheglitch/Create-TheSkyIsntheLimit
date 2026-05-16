package net.ctslteam.ctsl.event;

import dev.egg.SubLevelWarper;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
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

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID)
public final class CtslCommonEvents {

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

        if (source == sources.fellOutOfWorld()
                || source == sources.fall()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (player.tickCount % 10 != 0) return;

        var tracked = Sable.HELPER.getTrackingSubLevel(player);
        if (!(tracked instanceof ServerSubLevel subLevel)) return;

        double subLevelHeight = subLevel.logicalPose().position().y;

        ServerLevel space = player.server.getLevel(
                ResourceKey.create(
                        Registries.DIMENSION,
                        ResourceLocation.fromNamespaceAndPath("ctsl", "space")
                )
        );

        if (subLevelHeight >= CtslServer.SPACE_HEIGHT.get() && !CtslDimensions.isSpace(player.level())) {
            CreateTheSkyIsnttheLimit.LOGGER.info("Player : " + player.getName().getString() + " is in space with this sub level : " + subLevel.getName());
            SubLevelWarper.WarpSubLevel(subLevel, space, new Vector3d(0, 10, 0));
        }
    }
}