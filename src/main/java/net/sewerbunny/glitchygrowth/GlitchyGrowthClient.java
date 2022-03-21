package net.sewerbunny.glitchygrowth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.sewerbunny.glitchygrowth.block.ModBlocks;

public class GlitchyGrowthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Cutout
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.DEAD_GRASS, ModBlocks.DEAD_GRASS_BLOCK);

        // Tint blocks
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0xbba577, ModBlocks.DEAD_GRASS);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0xbba577, ModBlocks.DEAD_GRASS_BLOCK);

        // Tint items
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0xbba577, ModBlocks.DEAD_GRASS);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0xbba577, ModBlocks.DEAD_GRASS_BLOCK);
    }
}
