package com.bestinslot.pacmodworld2.client.bdoing;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BDoingBlockRenderer extends GeoBlockRenderer<BDoingBlockEntity> {
    public BDoingBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(PacModWorld2.MOD_ID, "bdoing")));
    }

    @Override
    public AABB getRenderBoundingBox(BDoingBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(2.0, 0.0, 2.0);
    }
}
