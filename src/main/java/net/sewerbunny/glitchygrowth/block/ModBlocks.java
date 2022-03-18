package net.sewerbunny.glitchygrowth.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;
import net.sewerbunny.glitchygrowth.block.custom.DeadPlantBlock;
import net.sewerbunny.glitchygrowth.item.ModItemGroup;

public class ModBlocks {
    public static final Block DEAD_GRASS = registerBlock("dead_grass",
            new DeadPlantBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.OAK_TAN)
                    .noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), 7),
            ModItemGroup.GLITCHY_GROWTH);

    public static final Block DEAD_GRASS_BLOCK = registerBlock("dead_grass_block",
            new Block(FabricBlockSettings.of(Material.SOIL, MapColor.OAK_TAN)
                    .ticksRandomly().strength(0.6F).sounds(BlockSoundGroup.GRAVEL)),
            ModItemGroup.GLITCHY_GROWTH);


    @SuppressWarnings("SameParameterValue")
    private static Block registerBlock(String name, Block block, net.minecraft.item.ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(GlitchyGrowth.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, ItemGroup group) {
        Registry.register(Registry.ITEM,
                new Identifier(GlitchyGrowth.MOD_ID, name),
                new BlockItem(block,
                        new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks() {
        GlitchyGrowth.LOGGER.info("Registering mod blocks for " + GlitchyGrowth.MOD_ID);
    }
}
