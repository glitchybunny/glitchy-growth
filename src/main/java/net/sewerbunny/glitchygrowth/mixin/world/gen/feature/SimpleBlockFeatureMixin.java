package net.sewerbunny.glitchygrowth.mixin.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SimpleBlockFeature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(SimpleBlockFeature.class)
public class SimpleBlockFeatureMixin extends Feature<SimpleBlockFeatureConfig> {
    public SimpleBlockFeatureMixin(Codec<SimpleBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SimpleBlockFeatureConfig> context) {
        SimpleBlockFeatureConfig simpleBlockFeatureConfig = (SimpleBlockFeatureConfig) context.getConfig();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        BlockState blockState = simpleBlockFeatureConfig.toPlace().getBlockState(context.getRandom(), blockPos);
        if (blockState.canPlaceAt(structureWorldAccess, blockPos)) {
            if (blockState.getBlock() instanceof TallPlantBlock) {
                if (!structureWorldAccess.isAir(blockPos.up())) {
                    return false;
                }
                TallPlantBlock.placeAt(structureWorldAccess, blockState, blockPos, 2);
            } else if (blockState.getBlock() instanceof FernBlock) {
                // Ensure grass spawns with random age
                structureWorldAccess.setBlockState(blockPos, blockState.with(Properties.AGE_7, context.getRandom().nextInt(8)), 2);
            } else {
                structureWorldAccess.setBlockState(blockPos, blockState, 2);
            }

            return true;
        } else {
            return false;
        }
    }
}
