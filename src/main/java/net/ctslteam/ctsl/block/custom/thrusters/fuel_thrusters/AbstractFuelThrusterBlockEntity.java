package net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters;

import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class AbstractFuelThrusterBlockEntity extends AbstractThrusterBlockEntity {

    public AbstractFuelThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static FluidStack REQUIRED_FUEL = new FluidStack(Fluids.LAVA, 1);

    @Override
    public void sable$tick(ServerSubLevel subLevel) {
        if (ThrusterActive) {
            //IFluidHandler fluidHandler = worldIn.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, Direction.DOWN);
            IFluidHandler fluidHandler = subLevel.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().above(), Direction.DOWN);

            if (FluidStack.isSameFluid(REQUIRED_FUEL, fluidHandler.getFluidInTank(0))) {
                fluidHandler.drain(10, IFluidHandler.FluidAction.EXECUTE);
                Active = true;
                level.setBlock(getBlockPos(), getBlockState(), Block.UPDATE_NEIGHBORS);
            } else {
                Active = false;
                level.setBlock(getBlockPos(), getBlockState(), Block.UPDATE_NEIGHBORS);
            }
        }
    }
}
