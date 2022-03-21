package net.sewerbunny.glitchygrowth.block.custom;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class DeadPlantBlock extends PlantBlock {
    private static final IntProperty AGE = Properties.AGE_3;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)};

    public DeadPlantBlock(Settings settings, int defaultAge) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(AGE, defaultAge));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(this.getAgeProperty());
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XYZ;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())].offset(vec3d.x, vec3d.y, vec3d.z);
    }
}
