package com.bestinslot.pacmodworld2.registry;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PacModWorld2.MOD_ID);

    public static final DeferredBlock<BDoingBlock> BDOING = BLOCKS.registerBlock("bdoing", BDoingBlock::new,
        BlockBehaviour.Properties.of()
            .strength(0.8f)
            .sound(SoundType.SLIME_BLOCK)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK));

    private ModBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
