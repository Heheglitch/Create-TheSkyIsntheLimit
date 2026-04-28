package net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters;

import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class AbstractFuelThrusterBlock extends AbstractThrusterBlock{
    public AbstractFuelThrusterBlock(Properties properties) {
        super(properties);
    }

    public static FluidStack REQUIRED_FUEL = new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME);
    public static int REQUIRED_FUEL_AMOUNT = 1;

    @Override
    protected void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos, boolean movedByPiston) {
        if (worldIn.isClientSide)
            return;

        boolean previouslyPowered = state.getValue(POWERED);

        if (previouslyPowered != worldIn.hasNeighborSignal(pos)) {
            worldIn.setBlock(pos, state.cycle(POWERED), Block.UPDATE_CLIENTS);
            AbstractFuelThrusterBlockEntity.ThrusterActive = worldIn.hasNeighborSignal(pos);
        }
    }
}
