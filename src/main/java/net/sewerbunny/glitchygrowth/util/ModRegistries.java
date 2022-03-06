package net.sewerbunny.glitchygrowth.util;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.sewerbunny.glitchygrowth.GlitchyGrowth;

public class ModRegistries {
    public static void registerModStuffs() {
        registerFuels();
    }

    private static void registerFuels() {
        GlitchyGrowth.LOGGER.info("Registering fuels for " + GlitchyGrowth.MOD_ID);
        FuelRegistry registry = FuelRegistry.INSTANCE;


    }
}
