package net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters.hydrogen_thruster;

import com.mojang.serialization.MapCodec;
import net.ctslteam.ctsl.block.custom.thrusters.AbstractThrusterBlockEntity;
import net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters.AbstractFuelThrusterBlock;
import net.ctslteam.ctsl.index.CtslBlockEntityTypes;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class HydrogenThrusterBlock extends AbstractFuelThrusterBlock {
    public static final MapCodec<HydrogenThrusterBlock> CODEC = simpleCodec(HydrogenThrusterBlock::new);

    public HydrogenThrusterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<? extends AbstractThrusterBlockEntity> getBlockEntityType() {
        return CtslBlockEntityTypes.HYDROGEN_THRUSTER_BLOCK_ENTITY.get();
    }
}
