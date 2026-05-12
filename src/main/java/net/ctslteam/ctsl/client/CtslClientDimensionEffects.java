package net.ctslteam.ctsl.client;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.client.space.SpaceDimensionSpecialEffects;
import net.ctslteam.ctsl.dimension.CtslDimensions;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

/**
 * Client-only registration for custom dimension visual effects.
 */
@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = net.neoforged.api.distmarker.Dist.CLIENT)
public class CtslClientDimensionEffects {

    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        /*
         * IMPORTANT:
         * Register using the dimension TYPE id, not the dimension id.
         */
        ResourceLocation dimensionTypeId = CtslDimensions.SPACE_TYPE.location();
        event.register(dimensionTypeId, SpaceDimensionSpecialEffects.INSTANCE);
    }
}
