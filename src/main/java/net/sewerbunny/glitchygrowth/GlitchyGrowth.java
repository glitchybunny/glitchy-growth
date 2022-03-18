package net.sewerbunny.glitchygrowth;

import net.fabricmc.api.ModInitializer;
import net.sewerbunny.glitchygrowth.block.ModBlocks;
import net.sewerbunny.glitchygrowth.item.ModItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GlitchyGrowth implements ModInitializer {
	public static final String MOD_ID = "glitchygrowth";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}
