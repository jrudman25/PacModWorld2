package com.bestinslot.pacmodworld2.registry;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PacModWorld2.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BDoingBlockEntity>> BDOING = BLOCK_ENTITY_TYPES.register("bdoing",
        () -> BlockEntityType.Builder.of(BDoingBlockEntity::new, ModBlocks.BDOING.get()).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
