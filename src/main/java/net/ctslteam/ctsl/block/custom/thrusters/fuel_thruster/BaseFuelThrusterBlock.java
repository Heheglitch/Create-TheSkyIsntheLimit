package net.ctslteam.ctsl.block.custom.thrusters.fuel_thruster;

import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlock;
import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class BaseFuelThrusterBlock extends BaseThrusterBlock {
    public BaseFuelThrusterBlock(Properties properties) {
        super(properties);
    }

    public static FluidStack REQUIRED_FUEL = new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME);
    public static int REQUIRED_FUEL_AMOUNT = 500;

    @Override
    protected void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos, boolean movedByPiston) {
        if (worldIn.isClientSide)
            return;

        boolean previouslyPowered = state.getValue(POWERED);
        IFluidHandler fluidHandler = worldIn.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, Direction.DOWN);

        if (previouslyPowered != worldIn.hasNeighborSignal(pos)) {
            worldIn.setBlock(pos, state.cycle(POWERED), Block.UPDATE_CLIENTS);

            if (neighborPos.getY() == pos.getY() +1 && fluidHandler != null && worldIn.hasNeighborSignal(pos)) {
                int fluidTankID = 0;

                for (int tanks = fluidHandler.getTanks(); tanks > 0; tanks--) {
                    if (FluidStack.isSameFluid(REQUIRED_FUEL, fluidHandler.getFluidInTank(tanks))) {
                        fluidTankID = tanks;
                        break;
                    }
                }

                if (FluidStack.isSameFluid(REQUIRED_FUEL, fluidHandler.getFluidInTank(fluidTankID))) {
                    if (fluidHandler.getFluidInTank(fluidTankID).getAmount() >= REQUIRED_FUEL_AMOUNT) {
                        BaseThrusterBlockEntity.Active = worldIn.hasNeighborSignal(pos);
                        fluidHandler.drain(REQUIRED_FUEL_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            } else {
                BaseThrusterBlockEntity.Active = worldIn.hasNeighborSignal(pos);
            }
        }
    }
}
