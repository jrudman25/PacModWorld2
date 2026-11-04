package com.bestinslot.pacmodworld2.client.redrat;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.entity.redrat.RedRatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RedRatRenderer extends GeoEntityRenderer<RedRatEntity> {
    public RedRatRenderer(EntityRendererProvider.Context context) {
        super(context, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(PacModWorld2.MOD_ID, "redrat"), false));
        this.shadowRadius = 0.3F;
    }
}
