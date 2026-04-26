package net.ctslteam.ctsl.index;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.ctslteam.ctsl.CreateTheSkyIsnttheLimit;
import net.ctslteam.ctsl.block.CtslBlocks;
import net.ctslteam.ctsl.block.custom.thrusters.debug_thruster.CreativeThrusterBlockEntity;
import net.ctslteam.ctsl.block.custom.thrusters.fuel_thrusters.hydrogen_thruster.HydrogenThrusterBlockEntity;

public class CtslBlockEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = CreateTheSkyIsnttheLimit.getRegistrate();

    public static final BlockEntityEntry<CreativeThrusterBlockEntity> DEBUG_THRUSTER_BLOCK_ENTITY = REGISTRATE
            .blockEntity("debug_thruster", CreativeThrusterBlockEntity::new)
            .validBlocks(CtslBlocks.CREATIVE_THRUSTER)
            .register();

    public static final BlockEntityEntry<HydrogenThrusterBlockEntity> HYDROGEN_THRUSTER_BLOCK_ENTITY = REGISTRATE
            .blockEntity("hyrdogen_thruster", HydrogenThrusterBlockEntity::new)
            .validBlocks(CtslBlocks.HYDROGEN_THRUSTER)
            .register();

    public static void init() {

    }
}
