package net.ctslteam.ctsl.block.custom.thrusters.debug_thruster;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlock;
import net.ctslteam.ctsl.block.custom.thrusters.BaseThrusterBlockEntity;
import net.ctslteam.ctsl.index.CtslBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class DebugThrusterBlock extends BaseThrusterBlock {
    public static final MapCodec<DebugThrusterBlock> CODEC = simpleCodec(DebugThrusterBlock::new);

    public DebugThrusterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<? extends BaseThrusterBlockEntity> getBlockEntityType() {
        return CtslBlockEntityTypes.DEBUG_THRUSTER_BLOCK_ENTITY.get();
    }
}
