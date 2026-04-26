package net.ctslteam.ctsl;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    public static final ModConfigSpec.ConfigValue<Integer> CREATIVE_THRUSTER_THRUST = BUILDER
            .comment("The Creative Thruster Thrust")
            .define("Creative Thruster Thrust", 100);

    public static final ModConfigSpec.ConfigValue<Integer> HYDROGEN_THRUSTER_THRUST = BUILDER
            .comment("The Hydrogen Thruster Thrust")
            .define("Hydrogen Thruster Thrust", 70);

    static final ModConfigSpec SPEC = BUILDER.build();
}
