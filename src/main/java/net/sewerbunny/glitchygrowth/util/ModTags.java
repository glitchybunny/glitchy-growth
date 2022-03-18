package net.sewerbunny.glitchygrowth.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> GRASS = createCommonTag("grass");
        public static final TagKey<Block> DEAD_BLOCK = createCommonTag("dead_block");
        public static final TagKey<Block> DEAD_PLANT_BLOCK = createCommonTag("dead_plant_block");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(GlitchyGrowth.MOD_ID, name));
        }

        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier("c", name));
        }
    }

    public static class Items {


        private static TagKey<Item> createTag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier(GlitchyGrowth.MOD_ID, name));
        }

        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier("c", name));
        }
    }

    public static class EntityTypes {


        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(GlitchyGrowth.MOD_ID, name));
        }

        private static TagKey<EntityType<?>> createCommonTag(String name) {
            return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier("c", name));
        }
    }
}
