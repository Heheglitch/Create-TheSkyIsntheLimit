package net.ctslteam.ctsl.block.custom.thrusters.fuel_thruster.hydrogen_thruster;

import net.ctslteam.ctsl.Config;
import net.ctslteam.ctsl.block.custom.thrusters.fuel_thruster.BaseFuelThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HydrogenThrusterBlockEntity extends BaseFuelThrusterBlockEntity {

    public HydrogenThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public double getConfigThrust() {
        return Config.HYDROGEN_THRUSTER_THRUST.get();
    }
}
