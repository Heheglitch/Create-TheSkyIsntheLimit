package net.ctslteam.ctsl.block.custom.thrusters;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class BaseThrusterBlock extends DirectionalBlock implements IBE<BaseThrusterBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BaseThrusterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection())
                    .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
        } else {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite())
                    .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (state.getValue(POWERED)) {
            BaseThrusterBlockEntity.Active = true;
        } else {
            BaseThrusterBlockEntity.Active = false;
        }
    }

    @Override
    public Class<BaseThrusterBlockEntity> getBlockEntityClass() {
        return BaseThrusterBlockEntity.class;
    }
}
