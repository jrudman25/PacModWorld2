package com.bestinslot.pacmodworld2.registry;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PacModWorld2.MOD_ID);

    public static final DeferredItem<BDoingItem> BDOING = ITEMS.registerItem("bdoing",
        properties -> new BDoingItem(ModBlocks.BDOING.get(), properties));

    public static final DeferredItem<DeferredSpawnEggItem> RED_RAT_SPAWN_EGG = ITEMS.registerItem("red_rat_spawn_egg",
        properties -> new DeferredSpawnEggItem(ModEntities.RED_RAT, 0xB22222, 0x4A1010, properties));

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
