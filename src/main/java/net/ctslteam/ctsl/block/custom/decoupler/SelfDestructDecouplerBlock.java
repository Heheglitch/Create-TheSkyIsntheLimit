package net.ctslteam.ctsl.block.custom.decoupler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.minecraft.world.phys.AABB;

public class SelfDestructDecouplerBlock extends Block {

    public static final BooleanProperty POWERED =
            BlockStateProperties.POWERED;

    public SelfDestructDecouplerBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(POWERED,false)
        );
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean moving
    ) {

        if (level.isClientSide) return;

        boolean powered = level.hasNeighborSignal(pos);

        if (powered && !state.getValue(POWERED)) {

            level.scheduleTick(
                    pos,
                    this,
                    10
            );

            level.setBlock(
                    pos,
                    state.setValue(
                            POWERED,
                            true
                    ),
                    3
            );
        }
    }

    @Override
    public void tick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {

        if (!level.getEntitiesOfClass(
                Player.class,
                new AABB(pos)
        ).isEmpty()) {

            level.scheduleTick(
                    pos,
                    this,
                    10
            );

            return;
        }

        level.destroyBlock(
                pos,
                false
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(POWERED);
    }
}
