package net.ctslteam.ctsl.api.celestials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;
import java.util.Optional;

/**
 *This record store all kind of celestial specific things
 * @param id
 * @param type
 * @param linkedDimension
 * @param worldAnchor
 * @param renderRadius
 * @param collisionRadius
 * @param orbit
 */
public record CelestialDefinition(
        String id,
        CelestialType type,
        Optional<ResourceLocation> linkedDimension,
        Vec3 worldAnchor,
        double renderRadius,
        double collisionRadius,
        Optional<OrbitDefinition> orbit
) {
    public static final Codec<Vec3> VEC3_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(Vec3::x),
            Codec.DOUBLE.fieldOf("y").forGetter(Vec3::y),
            Codec.DOUBLE.fieldOf("z").forGetter(Vec3::z)
    ).apply(instance, Vec3::new));

    public static final Codec<CelestialType> TYPE_CODEC = Codec.STRING.xmap(
            s -> CelestialType.valueOf(s.toUpperCase(Locale.ROOT)),
            t -> t.name().toLowerCase(Locale.ROOT)
    );

    public static final Codec<CelestialDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(CelestialDefinition::id),
            TYPE_CODEC.fieldOf("type").forGetter(CelestialDefinition::type),
            ResourceLocation.CODEC.optionalFieldOf("linked_dimension").forGetter(CelestialDefinition::linkedDimension),
            VEC3_CODEC.fieldOf("world_anchor").forGetter(CelestialDefinition::worldAnchor),
            Codec.DOUBLE.optionalFieldOf("render_radius", 8000.0).forGetter(CelestialDefinition::renderRadius),
            Codec.DOUBLE.optionalFieldOf("collision_radius", 120.0).forGetter(CelestialDefinition::collisionRadius),
            OrbitDefinition.CODEC.optionalFieldOf("orbit").forGetter(CelestialDefinition::orbit)
    ).apply(instance, CelestialDefinition::new));
}