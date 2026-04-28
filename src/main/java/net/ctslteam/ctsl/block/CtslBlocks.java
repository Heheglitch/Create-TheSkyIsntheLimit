package net.ctslteam.ctsl.block;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.block.custom.thrusters.debug_thruster.CreativeThrusterBlock;
import net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters.hydrogen_thruster.HydrogenThrusterBlock;
import net.ctslteam.ctsl.block.custom.decoupler.SelfDestructDecouplerBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class CtslBlocks {
    private static final SimulatedRegistrate REGISTRATE = CreateTheSkyIsnttheLimit.getRegistrate();

    public static final TagKey<Block> SUPER_HEAVY = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("sable", "super_heavy")
    );
    public static final TagKey<Block> HEAVY = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("sable", "heavy")
    );
    public static final TagKey<Block> LIGHT = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("sable", "light")
    );
    public static final TagKey<Block> SUPER_LIGHT = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("sable", "super_light")
    );

    public static final BlockEntry<CreativeThrusterBlock> CREATIVE_THRUSTER =
            REGISTRATE.block("creative_thruster", CreativeThrusterBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(properties -> properties.noOcclusion().noTerrainParticles())
                    .lang("Creative Thruster")
                    .blockstate((c, p) -> p.directionalBlock(c.get(),
                            AssetLookup.partialBaseModel(c, p)))
                    .tag(SUPER_HEAVY)
                    .item()
                    .properties(properties -> properties.rarity(Rarity.EPIC))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<HydrogenThrusterBlock> HYDROGEN_THRUSTER =
            REGISTRATE.block("hydrogen_thruster", HydrogenThrusterBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(properties -> properties.noOcclusion().noTerrainParticles())
                    .lang("Hydrogen Thruster")
                    .blockstate((c, p) -> p.directionalBlock(c.get(),
                            AssetLookup.partialBaseModel(c, p), 180))
                    .tag(SUPER_HEAVY)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<SelfDestructDecouplerBlock> SELF_DESTRUCT_DECOUPLER =
        REGISTRATE.block("self_destruct_decoupler", SelfDestructDecouplerBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.noOcclusion())
                .lang("Self-Destruct Decoupler")
                .blockstate((c, p) -> p.simpleBlock(c.get(),
                        AssetLookup.partialBaseModel(c, p)))
                .item()
                .transform(customItemModel())
                .register();

    public static void init() {

    }
}
