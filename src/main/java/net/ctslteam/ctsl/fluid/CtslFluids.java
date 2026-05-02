package net.ctslteam.ctsl.fluid;

import com.simibubi.create.AllTags;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.eriksonn.aeronautics.util.AeroColors;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.createmod.catnip.theme.Color;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.fluid.hydrogen.HydrogenFluidSource;
import net.ctslteam.ctsl.fluid.hydrogen.HydrogenFluidType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class CtslFluids {
    private static final SimulatedRegistrate REGISTRATE = CreateTheSkyIsnttheLimit.getRegistrate();

    public static final FluidEntry<BaseFlowingFluid.Flowing> HYDROGEN_FLUID =
            REGISTRATE.standardFluid("hydrogen_fluid", CtslFluidType.create(AeroColors.LEVIBLEND_THE_FOG_IS_COMING,
                            () -> 1f / 32f * AllConfigs.client().chocolateTransparencyMultiplier.getF(),
                            HydrogenFluidType::new)
                            )
                    .lang("Liquefied Hydrogen")
                    .properties(properties -> properties.viscosity(0)
                            .canSwim(false).density(-500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(5)
                            .slopeFindDistance(4)
                            .explosionResistance(100f))
                    .tag(Tags.Fluids.WATER)
                    .source(HydrogenFluidSource::new)
                    .bucket()
                    .lang("Liquified Hydrogen Bucket")
                    .tag(Tags.Items.BUCKETS)
                    .build()
                    .register();

    public static void init() {

    }
}
