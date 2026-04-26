package net.ctslteam.ctsl.item;

import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.block.CtslBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateTheSkyIsnttheLimit.MOD_ID);

    public static final Supplier<CreativeModeTab> CTSL_MOD_TAB = CREATIVE_MODE_TAB.register("ctsl_mod_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CtslBlocks.CREATIVE_THRUSTER))
                    .title(Component.translatable("creativetab.ctsl.ctsl_mod_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(CtslBlocks.CREATIVE_THRUSTER);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
