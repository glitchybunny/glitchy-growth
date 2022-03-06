package net.sewerbunny.glitchygrowth.mixin;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.sewerbunny.glitchygrowth.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(GrassBlock.class)
public class GrassBlockMixin extends SpreadableBlock implements Fertilizable {
    protected GrassBlockMixin(Settings settings) {
        super(settings);
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getLevel() == 8) {
            return false;
        } else {
            int i = ChunkLightProvider.getRealisticOpacity(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
            return i < world.getMaxLightLevel();
        }
    }

    private static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
        } else {
            if (world.getLightLevel(pos.up()) >= 9) {
                // Vanilla grass spreading code
                BlockState blockState = this.getDefaultState();

                for (int i = 0; i < 4; ++i) {
                    BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (world.getBlockState(blockPos).isOf(Blocks.DIRT) && canSpread(blockState, world, blockPos)) {
                        world.setBlockState(blockPos, blockState.with(SNOWY, world.getBlockState(blockPos.up()).isOf(Blocks.SNOW)));
                    }
                }

                // Grow tall grass above
                if (isFertilizable(world, pos, state, true)) {
                    if (random.nextInt(16384) == 0) {
                        // Similar to the grow() command
                        BlockPos blockPos = pos.up();
                        RegistryEntry registryEntry;
                        if (random.nextInt(128) == 0) {
                            List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos).value().getGenerationSettings().getFlowerFeatures();
                            registryEntry = ((RandomPatchFeatureConfig) list.get(0).config()).feature();
                        } else {
                            registryEntry = VegetationPlacedFeatures.GRASS_BONEMEAL;
                        }
                        ((PlacedFeature) registryEntry.value()).generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos);
                    }
                } else if (world.getBlockState(pos.up()).isIn(ModTags.Blocks.GRASS_PLANTS)) {
                    if (random.nextInt(128) == 0) {
                        // Try to grow grass nearby
                        for (int i = 0; i < 4; ++i) {
                            BlockPos blockPos = pos.add(random.nextInt(5) - 2, random.nextInt(5) - 3, random.nextInt(5) - 2);
                            if (world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK) && isFertilizable(world, blockPos, blockState, true)) {
                                world.setBlockState(blockPos.up(), Blocks.GRASS.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
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
