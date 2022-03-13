package net.sewerbunny.glitchygrowth.mixin;

import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PlantBlock;
import org.spongepowered.asm.mixin.Mixin;

// I think this is #minecraft:small_flowers

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin extends PlantBlock {
    protected FlowerBlockMixin(Settings settings) {
        super(settings);
    }
}
