package net.ctslteam.ctsl.dimension;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "ctsl")
public class SpaceGravityHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        boolean inSpace = CtslDimensions.isSpace(player.level());

        player.setNoGravity(inSpace);

        if (inSpace) {
            player.fallDistance = 0.0f;
        }
    }
}
