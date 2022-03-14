package net.sewerbunny.glitchygrowth.mixin;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(FernBlock.class)
public class FernBlockMixin extends PlantBlock implements Fertilizable {
    // SPREAD CHANCE
    // Y * 68.27 = average time for grass to spread. if Y = 30 then
    // 30 * 68.27 = 1638 seconds = 34.1 minutes
    // so every 34 minutes, the grass will try spreading to adjacent tiles
    // and the more grass there is, the more chances, so the faster it'll spread
    // Todo: Make configurable with ModMenu
    final static int GRASS_SPREAD_CHANCE = 30; // 34.1 minutes
    final static int FERN_SPREAD_CHANCE = 50; // 53.9 minutes

    protected FernBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean isFern = state.isOf(Blocks.FERN);

        if (world.getLightLevel(pos) >= 9) {
            if (random.nextInt(isFern ? FERN_SPREAD_CHANCE : GRASS_SPREAD_CHANCE) == 0) {
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
