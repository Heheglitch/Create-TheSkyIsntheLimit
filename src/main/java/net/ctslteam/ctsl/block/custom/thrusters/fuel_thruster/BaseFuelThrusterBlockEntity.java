package net.ctslteam.ctsl.block.custom.thrusters.fuel_thruster;

import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseFuelThrusterBlockEntity extends BaseThrusterBlockEntity {

    public BaseFuelThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

}
