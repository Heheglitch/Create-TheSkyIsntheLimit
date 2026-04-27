package net.ctslteam.ctsl.block.custom.decoupler; // арбузики

import com.mojang.serialization.MapCodec;
import net.ctslteam.ctsl.block.custom.thrusters.debug_thruster.CreativeThrusterBlock;
import net.ctslteam.ctsl.index.CtslBlockShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SelfDestructDecouplerBlock extends DirectionalBlock {
    public static final MapCodec<SelfDestructDecouplerBlock> CODEC = simpleCodec(SelfDestructDecouplerBlock::new);

    public SelfDestructDecouplerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
        } else {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CtslBlockShapes.SELF_DESTRUCT_DECOUPLER.get(state.getValue(FACING));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        if (level.isClientSide) return;

        if (level.hasNeighborSignal(pos)) {
            level.destroyBlock(pos, false); 
        }
    }
}