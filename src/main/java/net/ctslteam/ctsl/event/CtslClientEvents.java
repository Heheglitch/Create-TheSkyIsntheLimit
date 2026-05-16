package net.ctslteam.ctsl.event;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.client.space.SpaceDimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

@EventBusSubscriber(modid = CreateTheSkyIsnttheLimit.MOD_ID, value = Dist.CLIENT)
public final class CtslClientEvents {


    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(CreateTheSkyIsnttheLimit.MOD_ID, "space"),
                new SpaceDimensionSpecialEffects()
        );
    }
}
