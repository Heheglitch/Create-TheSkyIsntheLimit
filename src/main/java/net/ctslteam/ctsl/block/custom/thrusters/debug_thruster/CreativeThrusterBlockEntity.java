package net.ctslteam.ctsl.block.custom.thrusters.debug_thruster;

import net.ctslteam.ctsl.config.CtslServer;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeThrusterBlockEntity extends AbstractThrusterBlockEntity {

    public CreativeThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public double getConfigThrust() {
        return CtslServer.CREATIVE_THRUSTER_THRUST.get();
    }
}
