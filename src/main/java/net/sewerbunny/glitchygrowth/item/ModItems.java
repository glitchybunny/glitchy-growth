package net.sewerbunny.glitchygrowth.item;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;
import net.sewerbunny.glitchygrowth.block.ModBlocks;

public class ModItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GlitchyGrowth.MOD_ID, name), item);
    }

    public static void registerModItems() {
        GlitchyGrowth.LOGGER.info("Registering mod items for " + GlitchyGrowth.MOD_ID);
    }

    // Register composting items
    public static void registerItemComposting(Item item, float levelIncreaseChance) {
        CompostingChanceRegistry.INSTANCE.add(item, levelIncreaseChance);
    }

    public static void registerCompostingItems() {
        registerItemComposting(ModBlocks.DEAD_GRASS.asItem(), 0.3F);
    }
}
