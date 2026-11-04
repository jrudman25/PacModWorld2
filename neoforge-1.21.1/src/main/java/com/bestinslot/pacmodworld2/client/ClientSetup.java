package com.bestinslot.pacmodworld2.client;

import com.bestinslot.pacmodworld2.client.bdoing.BDoingBlockRenderer;
import com.bestinslot.pacmodworld2.client.bdoing.BDoingItemRenderer;
import com.bestinslot.pacmodworld2.client.redrat.RedRatRenderer;
import com.bestinslot.pacmodworld2.registry.ModBlockEntities;
import com.bestinslot.pacmodworld2.registry.ModEntities;
import com.bestinslot.pacmodworld2.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public final class ClientSetup {
    private ClientSetup() {
    }

    public static void registerModBusEvents(IEventBus modEventBus) {
        modEventBus.addListener(ClientSetup::registerRenderers);
        modEventBus.addListener(ClientSetup::registerClientExtensions);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.BDOING.get(), BDoingBlockRenderer::new);
        event.registerEntityRenderer(ModEntities.RED_RAT.get(), RedRatRenderer::new);
    }

    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private BDoingItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new BDoingItemRenderer();
                }
                return this.renderer;
            }
        }, ModItems.BDOING.get());
    }
}
