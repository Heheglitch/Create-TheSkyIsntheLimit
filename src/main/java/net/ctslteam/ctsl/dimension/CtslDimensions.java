package net.ctslteam.ctsl.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class CtslDimensions {
    public static final ResourceKey<Level> SPACE = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath("ctsl", "space")
    );

    public static final ResourceKey<DimensionType> SPACE_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath("ctsl", "space")
    );

    public static boolean isSpace(Level level) {
        return level.dimension() == SPACE;
    }
}

