package com.bestinslot.pacmodworld2;

import com.bestinslot.pacmodworld2.client.ClientSetup;
import com.bestinslot.pacmodworld2.registry.ModBlockEntities;
import com.bestinslot.pacmodworld2.registry.ModBlocks;
import com.bestinslot.pacmodworld2.registry.ModCreativeTabs;
import com.bestinslot.pacmodworld2.registry.ModEntities;
import com.bestinslot.pacmodworld2.registry.ModItems;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(PacModWorld2.MOD_ID)
public class PacModWorld2 {
    public static final String MOD_ID = "pacmodworld2";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PacModWorld2(IEventBus modEventBus) {
        LOGGER.info("PacModWorld2 loading");

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.registerModBusEvents(modEventBus);
        }
    }
}
