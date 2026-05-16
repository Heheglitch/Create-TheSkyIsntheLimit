package net.ctslteam.ctsl.config;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class CtslServer {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Integer> CREATIVE_THRUSTER_THRUST = BUILDER
            .comment("The Creative Thruster Thrust")
            .define("Creative Thruster Thrust", 100);

    public static final ModConfigSpec.ConfigValue<Integer> HYDROGEN_THRUSTER_THRUST = BUILDER
            .comment("The Hydrogen Thruster Thrust")
            .define("Hydrogen Thruster Thrust", 70);

    public static final ModConfigSpec.ConfigValue<Integer> HYDROGEN_THRUSTER_CONSOMATION = BUILDER
            .comment("The Hydrogen Thruster Consomation in mb/t")
            .define("Hydrogen Thruster Consomation in mb/t", 1);

    public static final ModConfigSpec.ConfigValue<Integer> SPACE_HEIGHT = BUILDER
            .comment("The height for the overwolrd to teleport a sub level in space")
            .define("Space Height", 2048);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
