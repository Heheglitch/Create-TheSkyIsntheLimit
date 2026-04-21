package net.ctslteam.ctsl.item;

import net.ctslteam.ctsl.CreateTheSkyIsntthelimit;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CreateTheSkyIsntthelimit.MOD_ID);

    // An Example Custom Item
    // public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", Item::new, new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
