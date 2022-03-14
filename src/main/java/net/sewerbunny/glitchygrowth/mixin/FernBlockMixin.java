package net.sewerbunny.glitchygrowth.mixin;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(FernBlock.class)
public class FernBlockMixin extends PlantBlock implements Fertilizable {
    private static final int MAX_AGE = 7;
    private static final IntProperty AGE = Properties.AGE_7;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D)};

    // SPREAD CHANCE
    // Y * 68.27 = average time for grass to spread. if Y = 30 then
    // 30 * 68.27 = 1638 seconds = 34.1 minutes
    // so every 34 minutes, the grass will try spreading to adjacent tiles
    // and the more grass there is, the more chances, so the faster it'll spread
    // Todo: Make configurable with ModMenu
    private static final int GRASS_SPREAD_CHANCE = 30; // 34.1 minutes
    private static final int FERN_SPREAD_CHANCE = 50; // 53.9 minutes

    
    protected FernBlockMixin(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(this.getAgeProperty(), MAX_AGE));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean isFern = state.isOf(Blocks.FERN);

        if (world.getLightLevel(pos) >= 9) {
            // Growth code
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                if (random.nextInt(5) == 0) {
                    world.setBlockState(pos, this.withAge(age + 1), 2);
                }
            }

            // Spread code
            if (random.nextInt(isFern ? FERN_SPREAD_CHANCE : (int)(105.0f/(float)age)) == 0) {
                // Don't spread if there's already >=5 grass in the 3x3 area
                int i = 5;
                for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
                    if (world.getBlockState(blockPos).isOf(this)) {
                        --i;
                        if (i <= 0) {
                            return;
                        }
                    }
                }

                // Otherwise, spread to nearby blocks
                for (int j = 0; j < 3; ++j) {
                    BlockPos blockPos2 = pos.add(random.nextInt(5) - 2, random.nextInt(2) - random.nextInt(2), random.nextInt(5) - 2);
                    if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                        world.setBlockState(blockPos2, isFern ? Blocks.FERN.getDefaultState() : Blocks.GRASS.getDefaultState());
                    }
                }
            }
        }
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return MAX_AGE;
    }

    protected int getAge(BlockState state) {
        return state.get(this.getAgeProperty());
    }

    public BlockState withAge(int age) {
        return this.getDefaultState().with(this.getAgeProperty(), age);
    }

    public boolean isMature(BlockState state) {
        return state.get(this.getAgeProperty()) >= this.getMaxAge();
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
    }

    @Shadow
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    @Shadow
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return false;
    }

    @Shadow
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
