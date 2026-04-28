package net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters;

import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFuelThrusterBlockEntity extends AbstractThrusterBlockEntity {
    public AbstractFuelThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    //Variables
    public abstract Fluid requiredFuel();
    public abstract int requiredFuelAmount();
    private static BlockCapabilityCache<IFluidHandler, @Nullable Direction> aboveFluidHandlerCache;


    //On sable tick
    @Override
    public void sable$tick(ServerSubLevel subLevel) {
        //Do nothing on client side
        if (subLevel.getLevel().isClientSide) return;

        //some variables
        BlockState state = getBlockState();
        BlockPos pos = getBlockPos();

        //If the thruster isn't active and the block isn't powered set the thrust to false
        if (!state.getValue(AbstractFuelThrusterBlock.POWERED) && !ThrusterActive) {
            Active = false;
            return;
        }

        BlockPos abovePos = pos.above();

        //cache to do an optimized tank check
        if (aboveFluidHandlerCache == null) {
            aboveFluidHandlerCache = BlockCapabilityCache.create(
                    Capabilities.FluidHandler.BLOCK,
                    (ServerLevel) level,
                    abovePos,
                    Direction.DOWN
            );
        }

        //if the block above us isn't a tank, return
        IFluidHandler handler = aboveFluidHandlerCache.getCapability();
        if (handler == null) {
            return;
        }

        //define what fluid type we want and simulate it
        FluidStack wanted = new FluidStack(requiredFuel(), requiredFuelAmount());
        FluidStack simulated = handler.drain(wanted, IFluidHandler.FluidAction.SIMULATE);

        //if for any reason the simulated try fail return
        if (simulated.isEmpty() || !simulated.is(requiredFuel()) || simulated.getAmount() < 1) {
            Active = false;
            return;
        }

        //Drain for real
        FluidStack drained = handler.drain(wanted, IFluidHandler.FluidAction.EXECUTE);

        //if we drain something we changed and the thruster is on
        if (!drained.isEmpty()) {
            Active = true;
            this.setChanged();
        } else {
            //if we drain nothing deactivate the thruster
            Active = false;
        }
    }
}
