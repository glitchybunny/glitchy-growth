package net.sewerbunny.glitchygrowth.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;

public class ModBlocks {


    private static Block registerBlock(String name, Block block, net.minecraft.item.ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(GlitchyGrowth.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, net.minecraft.item.ItemGroup group) {
        return Registry.register(Registry.ITEM,
                new Identifier(GlitchyGrowth.MOD_ID, name),
                new BlockItem(block,
                        new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks() {
        GlitchyGrowth.LOGGER.info("Registering mod blocks for " + GlitchyGrowth.MOD_ID);
    }
}
