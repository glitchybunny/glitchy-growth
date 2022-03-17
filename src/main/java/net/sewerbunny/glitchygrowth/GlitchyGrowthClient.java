package net.sewerbunny.glitchygrowth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.sewerbunny.glitchygrowth.block.ModBlocks;

public class GlitchyGrowthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.DEAD_GRASS, ModBlocks.DEAD_GRASS_FLAT);
    }
}
