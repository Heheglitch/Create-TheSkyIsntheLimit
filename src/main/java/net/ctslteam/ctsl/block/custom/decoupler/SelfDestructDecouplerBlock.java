package net.ctslteam.ctsl.block.custom.decoupler; // арбузики

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SelfDestructDecouplerBlock extends Block {
    public SelfDestructDecouplerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        if (level.isClientSide) return;

        if (level.hasNeighborSignal(pos)) {
            level.destroyBlock(pos, false); 
        }
    }
}