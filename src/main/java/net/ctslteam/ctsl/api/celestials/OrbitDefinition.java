package net.ctslteam.ctsl.api.celestials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record OrbitDefinition(
        String center,
        double semiMajorAxis,
        double inclinationDeg,
        long periodTicks,
        double phaseDeg
) {
    public static final Codec<OrbitDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("center").forGetter(OrbitDefinition::center),
            Codec.DOUBLE.fieldOf("semi_major_axis").forGetter(OrbitDefinition::semiMajorAxis),
            Codec.DOUBLE.optionalFieldOf("inclination_deg", 0.0).forGetter(OrbitDefinition::inclinationDeg),
            Codec.LONG.fieldOf("period_ticks").forGetter(OrbitDefinition::periodTicks),
            Codec.DOUBLE.optionalFieldOf("phase_deg", 0.0).forGetter(OrbitDefinition::phaseDeg)
    ).apply(instance, OrbitDefinition::new));
}
