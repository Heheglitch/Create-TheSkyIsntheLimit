package net.ctslteam.ctsl;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.simulated_team.simulated.util.SimColors;
import net.createmod.catnip.lang.FontHelper;
import net.ctslteam.ctsl.block.CtslBlocks;
import net.ctslteam.ctsl.config.CtslServer;
import net.ctslteam.ctsl.fluid.CtslFluids;
import net.ctslteam.ctsl.index.CtslBlockEntityTypes;
import net.ctslteam.ctsl.item.CtslItems;
import net.ctslteam.ctsl.registry.CtslRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateTheSkyIsnttheLimit.MOD_ID)
public class CreateTheSkyIsnttheLimit {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "ctsl";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CreateTheSkyIsnttheLimit(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (CreateTheSkyIsnttheLimit) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, CtslServer.SPEC);

        //Get registrate so we can reference it in other classes
        getRegistrate().registerEventListeners(modEventBus);

        init();
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private static final NonNullSupplier<CtslRegistrate> REGISTRATE = NonNullSupplier.lazy(() ->
            (CtslRegistrate) new CtslRegistrate(CreateTheSkyIsnttheLimit.path(MOD_ID), MOD_ID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null));

    public static void init() {
        setTooltips();

        CtslBlocks.init();
        CtslBlockEntityTypes.init();
        CtslItems.init();
        CtslFluids.init();
    }

    public static CtslRegistrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static void setTooltips() {
        getRegistrate().setTooltipModifierFactory(item -> {
            final Rarity rarity = item.getDefaultInstance().getRarity();
            FontHelper.Palette color = FontHelper.Palette.STANDARD_CREATE;
            if (rarity == Rarity.EPIC)
                color = new FontHelper.Palette(TooltipHelper.styleFromColor(SimColors.EPIC_OURPLE), TooltipHelper.styleFromColor(rarity.color()));

            return new ItemDescription
                    .Modifier(item, color)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public static ResourceLocation path(final String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}