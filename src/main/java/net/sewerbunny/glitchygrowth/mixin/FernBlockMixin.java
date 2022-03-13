package net.sewerbunny.glitchygrowth.mixin;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

// This is supposed to apply to #c:grasses
// Maybe I can make the code tag specific somehow? I need to learn more about mixins lol.

@Mixin(FernBlock.class)
public class FernBlockMixin extends PlantBlock implements Fertilizable {
    protected FernBlockMixin(Settings settings) {super(settings);}

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
