package net.ctslteam.ctsl.block;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.block.custom.thrusters.debug_thruster.DebugThrusterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

public class CtslBlocks {
    private static final SimulatedRegistrate REGISTRATE = CreateTheSkyIsnttheLimit.getRegistrate();


    /* Example Block registry using the function registerBlock
    /* public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block",
    /*        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));
    */

    /*public static final DeferredBlock<Block> DEBUG_THRUSTER = registerBlock("debug_thruster",
            () -> new DebugThrusterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    // A function that register both the block and the blockItem using the registerBlockItem function
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    // A function that register a blockItem using a block and a name as Reference
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        CtslItems.ITEMS.registerSimpleBlockItem(name, block, new Item.Properties());
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
     */

    public static final BlockEntry<DebugThrusterBlock> DEBUG_THRUSTER =
            REGISTRATE.block("debug_thruster", DebugThrusterBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(properties -> properties.sound(SoundType.AMETHYST))
                    .lang("Debug Thruster")
                    .blockstate((c, p) -> p.directionalBlock(c.get(), p.models()
                                    .orientableVertical(c.getName(), p.modLoc("block/debug_thruster"), p.modLoc("block/debug_thruster_front"))))
                    .item()
                    .build()
                    .register();

    public static void init() {

    }
}
