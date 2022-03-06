package net.sewerbunny.glitchygrowth.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;

public class ModItemGroup {
    public static final net.minecraft.item.ItemGroup GLITCHY_GROWTH = FabricItemGroupBuilder.build(new Identifier(GlitchyGrowth.MOD_ID, "glitchygrowth"),
            () -> new ItemStack(Items.GRASS));
}
