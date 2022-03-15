package net.sewerbunny.glitchygrowth.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.sewerbunny.glitchygrowth.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin extends PlantBlock {
    // Todo: Make configurable with ModMenu
    private static final int FLOWER_SPREAD_CHANCE = 40; // 43.1 minutes

    protected FlowerBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getLightLevel(pos) >= 11) {
            if (random.nextInt(FLOWER_SPREAD_CHANCE) == 0) {
                // Don't spread if there's already >=5 of same flower in the 5x5 area
                int i = 5;
                for (BlockPos blockPos : BlockPos.iterate(pos.add(-2, -1, -2), pos.add(2, 1, 2))) {
                    if (world.getBlockState(blockPos).isOf(this)) {
                        --i;
                        if (i <= 0) {
                            return;
                        }
                    }
                }

                // Otherwise, spread to nearby blocks
                for (int j = 0; j < 2; ++j) {
                    BlockPos blockPos2 = pos.add(random.nextInt(5) - 2, random.nextInt(2) - random.nextInt(2), random.nextInt(5) - 2);
                    if (state.canPlaceAt(world, blockPos2) && (world.isAir(blockPos2) || world.getBlockState(blockPos2).isIn(ModTags.Blocks.GRASSES))) {
                        world.setBlockState(blockPos2, this.getDefaultState());
                    }
                }
            }
        }
    }
}
