package net.sewerbunny.glitchygrowth.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;

public class ModItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GlitchyGrowth.MOD_ID, name), item);
    }

    public static void registerModItems() {
        GlitchyGrowth.LOGGER.info("Registering mod items for " + GlitchyGrowth.MOD_ID);
    }
}
