package com.bestinslot.pacmodworld2.registry;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.entity.redrat.RedRatEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, PacModWorld2.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<RedRatEntity>> RED_RAT = ENTITY_TYPES.register("red_rat",
        () -> EntityType.Builder.of(RedRatEntity::new, MobCategory.MONSTER)
            .sized(0.5F, 0.5F)
            .fireImmune()
            .clientTrackingRange(8)
            .build("red_rat"));

    private ModEntities() {
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(ModEntities::onEntityAttributeCreation);
        modEventBus.addListener(ModEntities::onRegisterSpawnPlacements);
    }

    private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(RED_RAT.get(), RedRatEntity.createAttributes().build());
    }

    private static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(RED_RAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            RedRatEntity::checkRedRatSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
