package net.sewerbunny.glitchygrowth.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;

public class ModItems {
    public static final Item TINY_GRASS = registerItem(
            "tiny_grass",
            new Item(new FabricItemSettings().group(ModItemGroup.GLITCHY_GROWTH)));
    public static final Item SHORT_GRASS = registerItem(
            "short_grass",
            new Item(new FabricItemSettings().group(ModItemGroup.GLITCHY_GROWTH)));
    public static final Item MEDIUM_GRASS = registerItem(
            "medium_grass",
            new Item(new FabricItemSettings().group(ModItemGroup.GLITCHY_GROWTH)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GlitchyGrowth.MOD_ID, name), item);
    }

    public static void registerModItems() {
        GlitchyGrowth.LOGGER.info("Registering mod items for " + GlitchyGrowth.MOD_ID);
    }
}
