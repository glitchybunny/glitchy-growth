package net.sewerbunny.glitchygrowth.mixin.block;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FernBlock.class)
public class FernBlockMixin extends PlantBlock implements Fertilizable {
    // Todo: Make spread chance configurable with ModMenu
    private static final float GRASS_SPREAD_CHANCE = 110.0f;
    private static final float FERN_SPREAD_CHANCE = 50.0f;
    private static final IntProperty AGE = Properties.AGE_7;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D),
            Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)};

    public FernBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void Init(CallbackInfo ci) {
        setDefaultState(getStateManager().getDefaultState().with(AGE, Properties.AGE_7_MAX));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean isFern = state.isOf(Blocks.FERN);
        int age = this.getAge(state);
        int skyLight = MathHelper.clamp(world.getLightLevel(LightType.SKY, pos) - world.getAmbientDarkness(), 0, 15);

        if (skyLight >= 9) {
            // Growth code
            if (!this.isMature(state)) {
                if (random.nextInt(10 + 4 * (15 - skyLight)) == 0) {
                    world.setBlockState(pos, this.withAge(age + 1), 2);
                }
            } else if (random.nextInt((int) GRASS_SPREAD_CHANCE + 8 * (15 - skyLight)) == 0 && !isFern) {
                // Chance to propagate grass down
                if (world.getBlockState(pos.down()).isOf(Blocks.DIRT)) {
                    world.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState());
                }
            }

            // Spread code
            int spreadChance = (int) (isFern ? FERN_SPREAD_CHANCE : (GRASS_SPREAD_CHANCE / (float) (age + 1)));
            if (random.nextInt(spreadChance + 4 * (15 - skyLight)) == 0) {
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
                    BlockPos blockPos2 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
                    if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                        world.setBlockState(blockPos2, isFern ? Blocks.FERN.getDefaultState().with(AGE, 0) : Blocks.GRASS.getDefaultState().with(AGE, 0));
                    }
                }
            }
        }
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return Properties.AGE_7_MAX;
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
        Vec3d vec3d = state.getModelOffset(world, pos);
        if (state.isOf(Blocks.FERN)) {
            return AGE_TO_SHAPE[7].offset(vec3d.x, vec3d.y, vec3d.z);
        }
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())].offset(vec3d.x, vec3d.y, vec3d.z);
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
}
