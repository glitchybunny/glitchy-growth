package net.sewerbunny.glitchygrowth.mixin;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
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
    // Todo: Make configurable with ModMenu
    int GRASS_CHANCE = 20000;
    int FLOWER_CHANCE = 180000;

    // TODO: move spreading code to FernBlockMixin and FlowerBlockMixin, etc
    int GRASS_SPREAD_CHANCE = 24;
    int FLOWER_SPREAD_CHANCE = 56;

    // How these values work (on a superflat world)
    // chunk = 16 * 16 = 256 surface blocks
    // average tick time for a block = 68.27 seconds (1365.33 ticks)
    // average tick time for 256 surface blocks = 68.27/256 = 0.267 seconds (5.333 ticks)
    //
    // SPOT_CHANCE
    // will grow above a grass_block with a 1 in X chance
    // X * 0.267 = average time for growth in a superflat chunk
    // For example, if X = 20000 then
    // 20000 * 0.267 = 5340 seconds = 89 minutes
    // something will grow every ~1.5 hours in a chunk
    //
    // SPREAD_CHANCE
    // if there's a plant above, it can spread to other grass blocks in a 5x5x5 area (2 blocks taxicab)
    // Y * 68.27 = average time for a plant to spread
    // For example, if Y = 24, then
    // 24 * 68.27 = 1638 seconds = 27.3 minutes
    // the plant will try spreading to adjacent tiles
    // however, it can spread up to 4 times, and each new plant can also then get random ticks and spread
    // leading to a (limited) geometric growth
    // essentially, the more plants there are, the faster they can spread

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

                // Mod stuff
                if (isFertilizable(world, pos, state, true)) {
                    // Growth code
                    BlockPos blockPos = pos.up();
                    RegistryEntry registryEntry;

                    // Randomly grow plants above grass blocks
                    if (random.nextInt(GRASS_CHANCE) == 0) {
                        // Grow grass
                        registryEntry = VegetationPlacedFeatures.GRASS_BONEMEAL;
                        ((PlacedFeature) registryEntry.value()).generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos);
                    } else if (random.nextInt(FLOWER_CHANCE) == 0) {
                        // Grow a random feature (flowers, fern, etc)
                        List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos).value().getGenerationSettings().getFlowerFeatures();
                        registryEntry = ((RandomPatchFeatureConfig) list.get(0).config()).feature();
                        ((PlacedFeature) registryEntry.value()).generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos);
                    }

                } else if (world.getBlockState(pos.up()).isIn(ModTags.Blocks.GRASSES)) {
                    // Fern (grass) spreading code
                    if (random.nextInt(GRASS_SPREAD_CHANCE) == 0) {
                        for (int i = 0; i < 4; ++i) {
                            BlockPos blockPos = pos.add(random.nextInt(5) - 2, random.nextInt(5) - 3, random.nextInt(5) - 2);
                            if (world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK) && isFertilizable(world, blockPos, blockState, true)) {
                                world.setBlockState(blockPos.up(), Blocks.GRASS.getDefaultState());
                            }
                        }
                    }
                } else if (world.getBlockState(pos.up()).isIn(BlockTags.SMALL_FLOWERS)) {
                    // Feature spreading code
                    if (random.nextInt(FLOWER_SPREAD_CHANCE) == 0) {
                        for (int i = 0; i < 2; ++i) {
                            BlockPos blockPos = pos.add(random.nextInt(7) - 3, random.nextInt(3) - 1, random.nextInt(7) - 3);
                            if (world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK) && isFertilizable(world, blockPos, blockState, true)) {
                                world.setBlockState(blockPos.up(), world.getBlockState(pos.up()));
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
