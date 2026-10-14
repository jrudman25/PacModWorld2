package com.bestinslot.pacmodworld2;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(PacModWorld2.MOD_ID)
public class PacModWorld2 {
    public static final String MOD_ID = "pacmodworld2";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PacModWorld2(IEventBus modEventBus) {
        LOGGER.info("PacModWorld2 loading");
    }
}
