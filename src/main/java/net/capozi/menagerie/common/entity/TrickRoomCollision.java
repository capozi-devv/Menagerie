package net.capozi.menagerie.common.entity;

import net.capozi.menagerie.common.entity.object.TrickRoomEntity;
import net.capozi.menagerie.foundation.EntityInit;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TrickRoomCollision {
    public static final int SIDE_RADIUS = 25;
    public static final int ROOM_SIZE = SIDE_RADIUS * 2 + 1;

    private static final double MIN_MOVEMENT_LENGTH_SQUARED = 1.0E-9D;
    private static final double COLLISION_THICKNESS = 1.0D / 32.0D;
    private static final double ROOM_QUERY_PADDING = ROOM_SIZE + SIDE_RADIUS + 2.0D;

    private TrickRoomCollision() {
    }

    public static Box boundsCenteredAtFeet(BlockPos footBlock) {
        int minX = footBlock.getX() - SIDE_RADIUS;
        int minY = footBlock.getY();
        int minZ = footBlock.getZ() - SIDE_RADIUS;
        int maxX = minX + ROOM_SIZE;
        int maxY = minY + ROOM_SIZE;
        int maxZ = minZ + ROOM_SIZE;

        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static List<VoxelShape> collisionShapes(Entity entity, Box entityBoundingBox, World world, Vec3d movement) {
        if (movement.lengthSquared() < MIN_MOVEMENT_LENGTH_SQUARED) {
            return List.of();
        }

        Box sweptBox = entityBoundingBox.stretch(movement).expand(COLLISION_THICKNESS);
        List<VoxelShape> shapes = new ArrayList<>();
        for (TrickRoomEntity room : roomsIntersecting(world, sweptBox)) {
            appendOutgoingRoomShellShapes(room.getRoomBounds(), movement, sweptBox, shapes);
        }
        return shapes;
    }

    @Nullable
    public static BlockHitResult raycastBarrier(BlockView world, Vec3d start, Vec3d end) {
        if (!(world instanceof World actualWorld)) {
            return null;
        }

        BarrierHit nearestHit = null;
        Box rayBox = boxBetween(start, end).expand(COLLISION_THICKNESS);
        for (TrickRoomEntity room : roomsIntersecting(actualWorld, rayBox)) {
            nearestHit = nearestHit(nearestHit, raycastExitBounds(room.getRoomBounds(), start, end));
        }

        if (nearestHit == null) {
            return null;
        }
        return new BlockHitResult(nearestHit.position(), nearestHit.side(), BlockPos.ofFloored(nearestHit.position()), false);
    }

    public static boolean segmentIntersectsBarrierShell(World world, Vec3d start, Vec3d end, double distance) {
        double shellDistance = Math.max(distance, 0.0D);
        Box sweptBox = boxBetween(start, end).expand(shellDistance);
        for (TrickRoomEntity room : roomsIntersecting(world, sweptBox)) {
            if (segmentIntersectsShell(room.getRoomBounds(), start, end, shellDistance)) {
                return true;
            }
        }
        return false;
    }

    private static List<TrickRoomEntity> roomsIntersecting(World world, Box box) {
        return world.getEntitiesByType(
                EntityInit.TRICK_ROOM,
                box.expand(ROOM_QUERY_PADDING),
                room -> room.hasRoomBounds() && !room.isFadingOut() && room.getRoomBounds().intersects(box)
        );
    }

    private static Box boxBetween(Vec3d start, Vec3d end) {
        return new Box(
                Math.min(start.x, end.x),
                Math.min(start.y, end.y),
                Math.min(start.z, end.z),
                Math.max(start.x, end.x),
                Math.max(start.y, end.y),
                Math.max(start.z, end.z)
        );
    }

    private static void appendOutgoingRoomShellShapes(Box bounds, Vec3d movement, Box sweptBox, List<VoxelShape> shapes) {
        double halfThickness = COLLISION_THICKNESS * 0.5D;
        if (movement.x < 0.0D) {
            addShapeIfNeeded(shapes, sweptBox, new Box(
                    bounds.minX - halfThickness,
                    bounds.minY - halfThickness,
                    bounds.minZ - halfThickness,
                    bounds.minX + halfThickness,
                    bounds.maxY + halfThickness,
                    bounds.maxZ + halfThickness
            ));
        } else if (movement.x > 0.0D) {
            addShapeIfNeeded(shapes, sweptBox, new Box(
                    bounds.maxX - halfThickness,
                    bounds.minY - halfThickness,
                    bounds.minZ - halfThickness,
                    bounds.maxX + halfThickness,
                    bounds.maxY + halfThickness,
                    bounds.maxZ + halfThickness
            ));
        }

        if (movement.y < 0.0D) {
            addShapeIfNeeded(shapes, sweptBox, new Box(
                    bounds.minX - halfThickness,
                    bounds.minY - halfThickness,
                    bounds.minZ - halfThickness,
                    bounds.maxX + halfThickness,
                    bounds.minY + halfThickness,
                    bounds.maxZ + halfThickness
            ));
        } else if (movement.y > 0.0D) {
            addShapeIfNeeded(shapes, sweptBox, new Box(
                    bounds.minX - halfThickness,
                    bounds.maxY - halfThickness,
                    bounds.minZ - halfThickness,
                    bounds.maxX + halfThickness,
                    bounds.maxY + halfThickness,
                    bounds.maxZ + halfThickness
            ));
        }

        if (movement.z < 0.0D) {
            addShapeIfNeeded(shapes, sweptBox, new Box(
                    bounds.minX - halfThickness,
                    bounds.minY - halfThickness,
                    bounds.minZ - halfThickness,
                    bounds.maxX + halfThickness,
                    bounds.maxY + halfThickness,
                    bounds.minZ + halfThickness
            ));
        } else if (movement.z > 0.0D) {
            addShapeIfNeeded(shapes, sweptBox, new Box(
                    bounds.minX - halfThickness,
                    bounds.minY - halfThickness,
                    bounds.maxZ - halfThickness,
                    bounds.maxX + halfThickness,
                    bounds.maxY + halfThickness,
                    bounds.maxZ + halfThickness
            ));
        }
    }

    private static void addShapeIfNeeded(List<VoxelShape> shapes, Box sweptBox, Box shapeBox) {
        if (sweptBox.intersects(shapeBox)) {
            shapes.add(VoxelShapes.cuboid(shapeBox));
        }
    }

    private static boolean segmentIntersectsShell(Box bounds, Vec3d start, Vec3d end, double distance) {
        if (isWithinShell(bounds, start, distance) || isWithinShell(bounds, end, distance)) {
            return true;
        }

        Box outer = bounds.expand(distance);
        if (!segmentIntersectsBox(outer, start, end)) {
            return false;
        }

        Box inner = bounds.contract(distance);
        return !inner.contains(start) || !inner.contains(end);
    }

    private static boolean isWithinShell(Box bounds, Vec3d position, double distance) {
        Box outer = bounds.expand(distance);
        if (!outer.contains(position)) {
            return false;
        }

        Box inner = bounds.contract(distance);
        return !inner.contains(position);
    }

    private static boolean segmentIntersectsBox(Box bounds, Vec3d start, Vec3d end) {
        return bounds.contains(start) || bounds.contains(end) || raycastBounds(bounds, start, end) != null;
    }

    @Nullable
    private static BarrierHit nearestHit(@Nullable BarrierHit current, @Nullable BarrierHit candidate) {
        if (candidate == null) {
            return current;
        }
        if (current == null || candidate.distanceSquared() < current.distanceSquared()) {
            return candidate;
        }
        return current;
    }

    @Nullable
    private static BarrierHit raycastBounds(Box bounds, Vec3d start, Vec3d end) {
        boolean startsInside = bounds.contains(start);
        if (startsInside && bounds.contains(end)) {
            return null;
        }

        Vec3d delta = end.subtract(start);
        double[] range = {0.0D, 1.0D};
        Direction[] sides = {Direction.NORTH, Direction.SOUTH};

        if (!clipAxis(start.x, delta.x, bounds.minX, bounds.maxX, Direction.WEST, Direction.EAST, range, sides)
                || !clipAxis(start.y, delta.y, bounds.minY, bounds.maxY, Direction.DOWN, Direction.UP, range, sides)
                || !clipAxis(start.z, delta.z, bounds.minZ, bounds.maxZ, Direction.NORTH, Direction.SOUTH, range, sides)) {
            return null;
        }

        double hitT = startsInside ? range[1] : range[0];
        if (hitT < 0.0D || hitT > 1.0D) {
            return null;
        }

        Direction side = startsInside ? sides[1] : sides[0];
        Vec3d position = start.add(delta.multiply(hitT));
        return new BarrierHit(position, side, start.squaredDistanceTo(position));
    }

    @Nullable
    private static BarrierHit raycastExitBounds(Box bounds, Vec3d start, Vec3d end) {
        boolean endsInside = bounds.contains(end);
        if (endsInside) {
            return null;
        }

        Vec3d delta = end.subtract(start);
        double[] range = {0.0D, 1.0D};
        Direction[] sides = {Direction.NORTH, Direction.SOUTH};

        if (!clipAxis(start.x, delta.x, bounds.minX, bounds.maxX, Direction.WEST, Direction.EAST, range, sides)
                || !clipAxis(start.y, delta.y, bounds.minY, bounds.maxY, Direction.DOWN, Direction.UP, range, sides)
                || !clipAxis(start.z, delta.z, bounds.minZ, bounds.maxZ, Direction.NORTH, Direction.SOUTH, range, sides)) {
            return null;
        }

        double hitT = range[1];
        if (hitT < 0.0D || hitT > 1.0D || range[1] <= Math.max(range[0], 0.0D)) {
            return null;
        }

        Direction side = sides[1];
        Vec3d position = start.add(delta.multiply(hitT));
        return new BarrierHit(position, side, start.squaredDistanceTo(position));
    }

    private static boolean clipAxis(double start, double delta, double min, double max, Direction minSide,
                                    Direction maxSide, double[] range, Direction[] sides) {
        if (Math.abs(delta) < 1.0E-8D) {
            return start >= min && start <= max;
        }

        double near = (min - start) / delta;
        double far = (max - start) / delta;
        Direction nearSide = minSide;
        Direction farSide = maxSide;
        if (near > far) {
            double swap = near;
            near = far;
            far = swap;
            nearSide = maxSide;
            farSide = minSide;
        }

        if (near > range[0]) {
            range[0] = near;
            sides[0] = nearSide;
        }
        if (far < range[1]) {
            range[1] = far;
            sides[1] = farSide;
        }
        return range[0] <= range[1] && range[1] >= 0.0D && range[0] <= 1.0D;
    }

    private record BarrierHit(Vec3d position, Direction side, double distanceSquared) {
    }
}
