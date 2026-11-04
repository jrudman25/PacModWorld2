package com.bestinslot.pacmodworld2.client.bdoing;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BDoingItemRenderer extends GeoItemRenderer<BDoingItem> {
    public BDoingItemRenderer() {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(PacModWorld2.MOD_ID, "bdoing")));
    }
}
