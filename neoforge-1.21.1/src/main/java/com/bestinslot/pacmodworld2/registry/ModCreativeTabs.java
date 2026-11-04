package com.bestinslot.pacmodworld2.registry;

import com.bestinslot.pacmodworld2.PacModWorld2;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PacModWorld2.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PACMODWORLD2 = CREATIVE_MODE_TABS.register("pacmodworld2",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.pacmodworld2"))
            .icon(() -> new ItemStack(ModItems.BDOING.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.BDOING.get());
                output.accept(ModItems.RED_RAT_SPAWN_EGG.get());
            })
            .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
