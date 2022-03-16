package net.sewerbunny.glitchygrowth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Items;
import net.sewerbunny.glitchygrowth.item.ModItems;

public class GlitchyGrowthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Colour items
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x7CBD6B, ModItems.TINY_GRASS);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x7CBD6B, ModItems.SHORT_GRASS);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x7CBD6B, ModItems.MEDIUM_GRASS);
    }
}
