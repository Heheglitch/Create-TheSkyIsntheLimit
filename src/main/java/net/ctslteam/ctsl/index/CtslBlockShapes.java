package net.ctslteam.ctsl.index;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

public class CtslBlockShapes {

    //Non-Static Block Shape
    public static final VoxelShaper
    SELF_DESTRUCT_DECOUPLER = shape(0, 0, 0, 16, 16, 16)
            .erase(2, 0, 2, 14, 16, 14).forDirectional()

            ;




    private static CtslBlockShapes.Builder shape(final VoxelShape shape) {
        return new CtslBlockShapes.Builder(shape);
    }

    private static CtslBlockShapes.Builder shape(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {
        private VoxelShape shape;

        public Builder(final VoxelShape shape) {
            this.shape = shape;
        }

        public CtslBlockShapes.Builder add(final VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public CtslBlockShapes.Builder add(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
            return this.add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public CtslBlockShapes.Builder erase(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
            this.shape = Shapes.join(this.shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return this.shape;
        }

        public VoxelShaper build(final BiFunction<VoxelShape, Direction, VoxelShaper> factory, final Direction direction) {
            return factory.apply(this.shape, direction);
        }

        public VoxelShaper build(final BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, final Direction.Axis axis) {
            return factory.apply(this.shape, axis);
        }

        public VoxelShaper forAxis() {
            return this.build(VoxelShaper::forAxis, Direction.Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return this.build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }

        public VoxelShaper forHorizontal(final Direction direction) {
            return this.build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional(final Direction direction) {
            return this.build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forDirectional() {
            return this.forDirectional(Direction.UP);
        }
    }
}

