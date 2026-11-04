package com.bestinslot.pacmodworld2.block.bdoing;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;

public enum BDoingPart implements StringRepresentable {
    NORTH_WEST(-1, -1),
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    WEST(-1, 0),
    CENTER(0, 0),
    EAST(1, 0),
    SOUTH_WEST(-1, 1),
    SOUTH(0, 1),
    SOUTH_EAST(1, 1);

    public static final List<BDoingPart> PARTS = List.of(values());

    private final int dx;
    private final int dz;
    private final String serializedName;

    BDoingPart(int dx, int dz) {
        this.dx = dx;
        this.dz = dz;
        this.serializedName = name().toLowerCase();
    }

    @Override
    public String getSerializedName() {
        return this.serializedName;
    }

    public BlockPos offsetFrom(BlockPos center) {
        return center.offset(this.dx, 0, this.dz);
    }

    public BlockPos centerPos(BlockPos partPos) {
        return partPos.offset(-this.dx, 0, -this.dz);
    }

    public static List<BlockPos> allPositions(BlockPos center) {
        return PARTS.stream().map(part -> part.offsetFrom(center)).toList();
    }
}
