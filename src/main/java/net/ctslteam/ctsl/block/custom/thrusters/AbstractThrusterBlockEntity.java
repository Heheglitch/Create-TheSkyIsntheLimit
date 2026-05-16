package net.ctslteam.ctsl.block.custom.thrusters;

import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.ctslteam.ctsl.api.sable.BlockEntitySubLevelThrusterActor;
import net.ctslteam.ctsl.api.sable.BlockEntityThruster;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractThrusterBlockEntity extends BlockEntity implements BlockEntitySubLevelThrusterActor, BlockEntityThruster {
    public AbstractThrusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static boolean ThrusterActive = false;

    public static boolean Active;

    public BlockEntityThruster getThruster() {
        return this;
    }

    public abstract double getConfigThrust();

    @Override
    public double getThrust() {
        return this.getConfigThrust();
    }

    @Override
    public Direction getBlockDirection() {
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public void sable$tick(ServerSubLevel subLevel) {
        Active = ThrusterActive;
    }

    @Override
    public boolean isActive() {
        return Active;
    }
}
