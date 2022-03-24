package net.sewerbunny.glitchygrowth.block.custom;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class HollowPillarBlock extends PillarBlock implements Waterloggable {
    private static final VoxelShape OUTLINE_SHAPE;
    protected static final VoxelShape X_SHAPE;
    protected static final VoxelShape Y_SHAPE;
    protected static final VoxelShape Z_SHAPE;
    protected static final VoxelShape X_COLLISION_SHAPE;
    protected static final VoxelShape Y_COLLISION_SHAPE;
    protected static final VoxelShape Z_COLLISION_SHAPE;
    public static final BooleanProperty WATERLOGGED;

    public HollowPillarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> X_SHAPE;
            case Y -> Y_SHAPE;
            case Z -> Z_SHAPE;
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> X_COLLISION_SHAPE;
            case Y -> Y_COLLISION_SHAPE;
            case Z -> Z_COLLISION_SHAPE;
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        OUTLINE_SHAPE = VoxelShapes.fullCube();
        X_SHAPE = VoxelShapes.combineAndSimplify(
                VoxelShapes.fullCube(),
                createCuboidShape(0.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D),
                BooleanBiFunction.ONLY_FIRST);
        Y_SHAPE = VoxelShapes.combineAndSimplify(
                VoxelShapes.fullCube(),
                createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                BooleanBiFunction.ONLY_FIRST);
        Z_SHAPE = VoxelShapes.combineAndSimplify(
                VoxelShapes.fullCube(),
                createCuboidShape(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 16.0D),
                BooleanBiFunction.ONLY_FIRST);
        X_COLLISION_SHAPE = VoxelShapes.combineAndSimplify(
                VoxelShapes.fullCube(),
                createCuboidShape(0.0D, 2.0D, 2.0D, 16.0D, 13.0D, 14.0D),
                BooleanBiFunction.ONLY_FIRST);
        Y_COLLISION_SHAPE = VoxelShapes.combineAndSimplify(
                VoxelShapes.fullCube(),
                createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
                BooleanBiFunction.ONLY_FIRST);
        Z_COLLISION_SHAPE = VoxelShapes.combineAndSimplify(
                VoxelShapes.fullCube(),
                createCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 13.0D, 16.0D),
                BooleanBiFunction.ONLY_FIRST);
    }
}
