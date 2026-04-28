package net.ctslteam.ctsl.fluid;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class CtslFluids {
    private static final SimulatedRegistrate REGISTRATE = CreateTheSkyIsnttheLimit.getRegistrate();

    public static final FluidEntry<BaseFlowingFluid.Flowing> HYDROGEN_FLUID =
            REGISTRATE.standardFluid("hydrogen_fluid")
                    .lang("Hydrogen")
                    .properties(properties -> properties.viscosity(0)
                            .canSwim(false).density(-500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(5)
                            .slopeFindDistance(4)
                            .explosionResistance(100f))
                    .tag(Tags.Fluids.WATER)
                    .source(BaseFlowingFluid.Source::new)
                    .block()
                    .build()
                    .bucket()
                    .tag(Tags.Items.BUCKETS)
                    .build()
                    .register();

    public static void init() {

    }
}
