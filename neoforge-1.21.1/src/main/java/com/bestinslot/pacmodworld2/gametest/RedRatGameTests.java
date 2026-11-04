package com.bestinslot.pacmodworld2.gametest;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.entity.redrat.FlameBreath;
import com.bestinslot.pacmodworld2.entity.redrat.RedRatEntity;
import com.bestinslot.pacmodworld2.registry.ModEntities;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(PacModWorld2.MOD_ID)
@PrefixGameTestTemplate(false)
public class RedRatGameTests {
    private static final int BREATH_Y = 1;

    private static void layGrass(GameTestHelper helper) {
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK);
            }
        }
    }

    @GameTest(template = "empty_9x12x9")
    public void breathIgnitesTargetInConeAndNotBlocks(GameTestHelper helper) {
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.GRASS_BLOCK);
            }
        }
        helper.setBlock(new BlockPos(4, 1, 6), Blocks.OAK_PLANKS);

        RedRatEntity rat = helper.spawnWithNoFreeWill(ModEntities.RED_RAT.get(), new Vec3(4.5, BREATH_Y, 2.5));
        Pig frontPig = helper.spawnWithNoFreeWill(EntityType.PIG, new Vec3(4.5, BREATH_Y, 4.5));
        Pig rearPig = helper.spawnWithNoFreeWill(EntityType.PIG, new Vec3(4.5, BREATH_Y, 0.5));

        rat.lookAt(EntityAnchorArgument.Anchor.EYES, frontPig.getEyePosition());
        Vec3 aim = frontPig.getBoundingBox().getCenter().subtract(rat.getEyePosition()).normalize();
        FlameBreath.breathe(rat, aim);

        if (frontPig.getRemainingFireTicks() <= 0) {
            helper.fail("Front pig is not on fire");
        }
        if (frontPig.getHealth() >= frontPig.getMaxHealth()) {
            helper.fail("Front pig took no damage");
        }
        if (rearPig.getRemainingFireTicks() > 0) {
            helper.fail("Rear pig caught fire");
        }
        if (rearPig.getHealth() != rearPig.getMaxHealth()) {
            helper.fail("Rear pig took damage");
        }

        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                for (int y = 0; y < 12; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    helper.assertBlockState(pos, state -> !state.is(Blocks.FIRE), () -> "Fire block at " + pos);
                }
                BlockPos ground = new BlockPos(x, 0, z);
                helper.assertBlockState(ground, state -> state.is(Blocks.GRASS_BLOCK),
                    () -> "Ground changed at " + ground);
            }
        }
        helper.assertBlockState(new BlockPos(4, 1, 6), state -> state.is(Blocks.OAK_PLANKS), () -> "Planks changed");
        helper.succeed();
    }

    @GameTest(template = "empty_9x12x9")
    public void lootDropsZeroOrOneFireChargeWithoutLooting(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        RedRatEntity rat = helper.spawnWithNoFreeWill(ModEntities.RED_RAT.get(), new Vec3(4.5, BREATH_Y, 4.5));

        LootParams params = new LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, rat)
            .withParameter(LootContextParams.ORIGIN, rat.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().generic())
            .create(LootContextParamSets.ENTITY);
        LootTable table = level.getServer().reloadableRegistries()
            .getLootTable(ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(PacModWorld2.MOD_ID, "entities/red_rat")));

        boolean sawDrop = false;
        for (int i = 0; i < 50; i++) {
            List<ItemStack> drops = table.getRandomItems(params).stream().filter(stack -> !stack.isEmpty()).toList();
            if (drops.isEmpty()) {
                continue;
            }
            if (drops.size() != 1 || !drops.get(0).is(Items.FIRE_CHARGE) || drops.get(0).getCount() != 1) {
                helper.fail("Unexpected loot roll: " + drops);
            }
            sawDrop = true;
        }
        if (!sawDrop) {
            helper.fail("No fire_charge dropped in 50 rolls");
        }
        helper.succeed();
    }

    @GameTest(template = "empty_9x12x9")
    public void spawnRulesRequireGrass(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Difficulty original = level.getDifficulty();

        helper.setBlock(new BlockPos(4, 0, 4), Blocks.GRASS_BLOCK);
        helper.setBlock(new BlockPos(6, 0, 4), Blocks.STONE);
        BlockPos grassSpot = helper.absolutePos(new BlockPos(4, 1, 4));
        BlockPos stoneSpot = helper.absolutePos(new BlockPos(6, 1, 4));

        try {
            if (!RedRatEntity.checkRedRatSpawnRules(ModEntities.RED_RAT.get(), level, MobSpawnType.NATURAL, grassSpot, level.getRandom())) {
                helper.fail("checkRedRatSpawnRules false on grass");
            }
            if (RedRatEntity.checkRedRatSpawnRules(ModEntities.RED_RAT.get(), level, MobSpawnType.NATURAL, stoneSpot, level.getRandom())) {
                helper.fail("checkRedRatSpawnRules true on stone");
            }

            level.getServer().setDifficulty(Difficulty.PEACEFUL, true);
            if (RedRatEntity.checkRedRatSpawnRules(ModEntities.RED_RAT.get(), level, MobSpawnType.NATURAL, grassSpot, level.getRandom())) {
                helper.fail("checkRedRatSpawnRules true on Peaceful");
            }
        } finally {
            level.getServer().setDifficulty(original, true);
        }
        helper.succeed();
    }

    @GameTest(template = "empty_9x12x9", timeoutTicks = 60)
    public void ratHitsTallTargetAtCloseRange(GameTestHelper helper) {
        layGrass(helper);

        RedRatEntity rat = helper.spawn(ModEntities.RED_RAT.get(), new Vec3(4.5, BREATH_Y, 2.5));
        rat.setYRot(0.0F);
        rat.setYBodyRot(0.0F);
        rat.setYHeadRot(0.0F);
        Villager villager = helper.spawnWithNoFreeWill(EntityType.VILLAGER, new Vec3(4.5, BREATH_Y, 0.5));
        rat.setTarget(villager);

        helper.succeedWhen(() -> {
            if (villager.getHealth() >= villager.getMaxHealth() || villager.getRemainingFireTicks() <= 0) {
                throw new GameTestAssertException("Villager unhurt (health=" + villager.getHealth()
                    + ", fireTicks=" + villager.getRemainingFireTicks() + ")");
            }
        });
    }

    @GameTest(template = "empty_9x12x9")
    public void ratIsFireImmune(GameTestHelper helper) {
        RedRatEntity rat = helper.spawnWithNoFreeWill(ModEntities.RED_RAT.get(), new Vec3(4.5, BREATH_Y, 4.5));
        rat.igniteForSeconds(5.0F);
        helper.runAtTickTime(40, () -> {
            if (rat.getHealth() != rat.getMaxHealth()) {
                helper.fail("Red rat took fire damage");
            }
            helper.succeed();
        });
    }
}
