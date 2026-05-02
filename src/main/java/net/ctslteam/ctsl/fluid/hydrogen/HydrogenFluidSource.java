package net.ctslteam.ctsl.fluid.hydrogen;

import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class HydrogenFluidSource extends BaseFlowingFluid {
    public HydrogenFluidSource(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return 8;
    }
}
