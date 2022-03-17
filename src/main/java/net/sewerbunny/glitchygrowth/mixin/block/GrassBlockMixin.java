package net.sewerbunny.glitchygrowth.mixin.block;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(GrassBlock.class)
public class GrassBlockMixin extends SpreadableBlock implements Fertilizable {
    // CHANCE
    // average tick time per block = 68.27 seconds (1365.33 ticks)
    // average tick time for 16*16 chunk surface = 68.27/(16*16) = 0.267 seconds (5.333 ticks)
    // X * 0.267 = average time for growth in a chunk. if X = 40000 then
    // 40000 * 0.267 = 10680 seconds = 178 minutes (will grow on average every 3 hours)
    // Todo: Make configurable with ModMenu
    private static final int GRASS_CHANCE = 40000; // 3 hours
    private static final int FLOWER_CHANCE = 180000; // 13.35 hours

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
        } else if (world.getLightLevel(pos.up()) >= 9) {
            // Vanilla grass spreading behaviour (but 4x slower)
            if (random.nextInt(4) == 0) {
                BlockState blockState = this.getDefaultState();
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (world.getBlockState(blockPos).isOf(Blocks.DIRT) && canSpread(blockState, world, blockPos)) {
                        world.setBlockState(blockPos, blockState.with(SNOWY, world.getBlockState(blockPos.up()).isOf(Blocks.SNOW)));
                        // Also has a chance to seed grass above when spreading
                        if (random.nextInt(32) == 0) {
                            this.growGrass(world, pos);
                        }
                    }
                }
            }

            // Randomly grow plants above grass blocks
            if (isFertilizable(world, pos, state, world.isClient())) {
                if (random.nextInt(GRASS_CHANCE) == 0) {
                    this.growGrass(world, pos);
                } else if (random.nextInt(FLOWER_CHANCE) == 0) {
                    this.growFeature(world, random, pos);
                }
            }
        }
    }

    public void growGrass(ServerWorld world, BlockPos pos) {
        if (world.getBlockState(pos.up()).isOf(Blocks.AIR)) {
            world.setBlockState(pos.up(), Blocks.GRASS.getDefaultState().with(Properties.AGE_7, 0));
        }
    }

    public void growFeature(ServerWorld world, Random random, BlockPos pos) {
        RegistryEntry<PlacedFeature> registryEntry;
        List<ConfiguredFeature<?, ?>> list = world.getBiome(pos).value().getGenerationSettings().getFlowerFeatures();
        registryEntry = ((RandomPatchFeatureConfig) list.get(0).config()).feature();
        (registryEntry.value()).generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
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
