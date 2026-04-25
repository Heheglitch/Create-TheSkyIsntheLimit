package net.ctslteam.ctsl.block.custom.thrusters.debug_thruster;

import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DebugThrusterBlockEntity extends BaseThrusterBlockEntity {

    public DebugThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public double getThrust() {
        return 10;
    }
}
