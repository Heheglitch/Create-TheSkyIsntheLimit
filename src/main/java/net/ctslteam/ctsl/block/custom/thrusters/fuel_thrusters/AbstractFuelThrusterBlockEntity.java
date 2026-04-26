package net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters;

import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class AbstractFuelThrusterBlockEntity extends AbstractThrusterBlockEntity {

    public AbstractFuelThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }


    @Override
    public void sable$tick(ServerSubLevel subLevel) {
        if (Active) {
            //IFluidHandler fluidHandler = worldIn.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, Direction.DOWN);
            IFluidHandler fluidHandler = subLevel.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().above(), Direction.DOWN);


        }
    }
}
