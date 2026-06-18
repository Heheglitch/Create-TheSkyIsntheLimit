package net.ctslteam.ctsl.block.custom.thrusters;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

/**
 * This abstract class is the base of all "physical" Thrusters, it implements {@link AbstractThrusterBlockEntity}
 */
public abstract class AbstractThrusterBlock extends DirectionalBlock implements IBE<AbstractThrusterBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public AbstractThrusterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(FACING);
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
    protected void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos, boolean movedByPiston) {
        if (worldIn.isClientSide)
            return;

        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != worldIn.hasNeighborSignal(pos)) {
            worldIn.setBlock(pos, state.cycle(POWERED), Block.UPDATE_CLIENTS);
            //AbstractThrusterBlockEntity.Active = worldIn.hasNeighborSignal(pos);
            AbstractThrusterBlockEntity.ThrusterActive = worldIn.hasNeighborSignal(pos);
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public Class<AbstractThrusterBlockEntity> getBlockEntityClass() {
        return AbstractThrusterBlockEntity.class;
    }
}
