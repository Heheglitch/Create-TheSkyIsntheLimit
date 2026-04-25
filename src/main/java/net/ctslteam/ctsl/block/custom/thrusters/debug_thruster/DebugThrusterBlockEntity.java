package net.ctslteam.ctsl.block.custom.thrusters.debug_thruster;

import dev.eriksonn.aeronautics.content.blocks.propeller.small.BasePropellerBlock;
import net.ctslteam.ctsl.Config;
import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DebugThrusterBlockEntity extends BaseThrusterBlockEntity {

    public DebugThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public double getConfigThrust() {
        return Config.DEBUG_THRUSTER_THRUST.get();
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }
}
