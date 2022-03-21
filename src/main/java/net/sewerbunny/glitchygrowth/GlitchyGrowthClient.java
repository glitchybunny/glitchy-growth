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
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->
                view != null && pos != null ? getDeadGrassColor(view, pos) : 0xbba577, ModBlocks.DEAD_GRASS);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->
                view != null && pos != null ? getDeadGrassColor(view, pos) : 0xbba577, ModBlocks.DEAD_GRASS_BLOCK);

        // Tint items
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0xbba577, ModBlocks.DEAD_GRASS);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0xbba577, ModBlocks.DEAD_GRASS_BLOCK);
    }

    public int getDeadGrassColor(BlockRenderView view, BlockPos pos) {
        int grassColor = BiomeColors.getGrassColor(view, pos);
        int deadColor = 0xbba577;

        int r = avgColorChannel((grassColor >> 16) & 0xff, (deadColor >> 16) & 0xff);
        int g = avgColorChannel((grassColor >> 8) & 0xff, (deadColor >> 8) & 0xff);
        int b = avgColorChannel(grassColor & 0xff, deadColor & 0xff);

        return (0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    public int avgColorChannel(int channel1, int channel2) {
        return (int) Math.pow((Math.pow(channel1, 2.2) + Math.pow(channel2, 2.2) * 3) / 4, 1 / 2.2);
    }
}
