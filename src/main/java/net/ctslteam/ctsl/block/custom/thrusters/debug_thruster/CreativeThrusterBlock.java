package net.ctslteam.ctsl.block.custom.thrusters.debug_thruster;

import com.mojang.serialization.MapCodec;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlock;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlockEntity;
import net.ctslteam.ctsl.index.CtslBlockEntityTypes;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CreativeThrusterBlock extends AbstractThrusterBlock {
    public static final MapCodec<CreativeThrusterBlock> CODEC = simpleCodec(CreativeThrusterBlock::new);

    public CreativeThrusterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<? extends AbstractThrusterBlockEntity> getBlockEntityType() {
        return CtslBlockEntityTypes.DEBUG_THRUSTER_BLOCK_ENTITY.get();
    }
}
