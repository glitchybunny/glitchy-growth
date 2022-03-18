package net.sewerbunny.glitchygrowth.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FernBlock;
import net.minecraft.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(method = "hasRandomTicks()Z", at = @At("RETURN"), cancellable = true)
    private void hasRandomTicks(CallbackInfoReturnable<Boolean> cir) {
        Class<? extends Block> blockClass = this.getBlock().getClass();
        if (blockClass.equals(FernBlock.class) || blockClass.equals(FlowerBlock.class)) {
            cir.setReturnValue(true);
        }
    }

    @Shadow
    @SuppressWarnings("SameReturnValue")
    public Block getBlock() {
        return null;
    }
}
