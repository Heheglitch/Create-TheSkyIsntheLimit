package net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters.hydrogen_thruster;

import net.ctslteam.ctsl.config.CtslServer;
import net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters.AbstractFuelThrusterBlockEntity;
import net.ctslteam.ctsl.fluid.CtslFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class HydrogenThrusterBlockEntity extends AbstractFuelThrusterBlockEntity {

    public HydrogenThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public Fluid requiredFuel() {
        return CtslFluids.HYDROGEN_FLUID.getSource();
    }

    @Override
    public int requiredFuelAmount() {
        return CtslServer.HYDROGEN_THRUSTER_CONSOMATION.get();
    }

    @Override
    public double getConfigThrust() {
        return CtslServer.HYDROGEN_THRUSTER_THRUST.get();
    }
}
