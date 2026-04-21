package net.ctslteam.ctsl.block;

import net.ctslteam.ctsl.CreateTheSkyIsntthelimit;
import net.ctslteam.ctsl.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(CreateTheSkyIsntthelimit.MOD_ID);


    /* Example Block regestry using the function registerBlock
    /* public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block",
    /*        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));
    */

    // A function that register both the block and the blockItem using the registerBlockItem function
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    // A function that register a blockItem using a block and a name as Reference
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerSimpleBlockItem(name, block, new Item.Properties());
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
