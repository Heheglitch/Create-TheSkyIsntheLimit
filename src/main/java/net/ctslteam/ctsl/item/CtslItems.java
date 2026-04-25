package net.ctslteam.ctsl.item;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CtslItems {
    //public static final DeferredRegister.Items ITEMS =
    //        DeferredRegister.createItems(CreateTheSkyIsntthelimit.MOD_ID);

    private static final SimulatedRegistrate REGISTRATE = CreateTheSkyIsnttheLimit.getRegistrate();

    // An Example Custom Item
    // public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", Item::new, new Item.Properties());

    /*public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
     */

    public static final ItemEntry<Item> TEST_ITEM =
            REGISTRATE.item("test_item", Item::new)
            .lang("Test ITEM")
            .properties(properties -> properties.rarity(Rarity.EPIC))
            .register();



    public static void init() {

    }
}
