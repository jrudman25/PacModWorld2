package com.bestinslot.pacmodworld2.gametest;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingBlock;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingPart;
import com.bestinslot.pacmodworld2.registry.ModBlocks;
import com.bestinslot.pacmodworld2.registry.ModItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(PacModWorld2.MOD_ID)
@PrefixGameTestTemplate(false)
public class BDoingGameTests {
    private static final BlockPos CENTER = new BlockPos(4, 1, 4);

    @GameTest(template = "empty_9x12x9", timeoutTicks = 100)
    public void bounceLaunchesMobWithoutFallDamage(GameTestHelper helper) {
        BDoingBlock.placeAt(helper.getLevel(), helper.absolutePos(CENTER));
        Pig pig = helper.spawnWithNoFreeWill(EntityType.PIG, new BlockPos(4, 5, 4));

        double requiredY = helper.absolutePos(CENTER).getY() + 0.75 + 5.0;
        double[] maxY = { Double.NEGATIVE_INFINITY };

        helper.failIfEver(() -> {
            if (pig.getHealth() != pig.getMaxHealth()) {
                throw new GameTestAssertException("Pig took damage");
            }
        });
        helper.succeedWhen(() -> {
            maxY[0] = Math.max(maxY[0], pig.getY());
            if (maxY[0] < requiredY) {
                throw new GameTestAssertException("Pig has not reached apex yet (maxY=" + maxY[0] + ", required=" + requiredY + ")");
            }
        });
    }

    @GameTest(template = "empty_9x12x9", timeoutTicks = 60)
    public void standingStillDoesNotBounce(GameTestHelper helper) {
        BDoingBlock.placeAt(helper.getLevel(), helper.absolutePos(CENTER));
        Pig pig = helper.spawnWithNoFreeWill(EntityType.PIG, new Vec3(4.5, 1.75, 4.5));

        double padTop = helper.absolutePos(CENTER).getY() + 0.75;
        helper.failIfEver(() -> {
            if (pig.getY() > padTop + 0.5) {
                throw new GameTestAssertException("Standing pig bounced to y=" + pig.getY());
            }
        });
        helper.runAtTickTime(40, helper::succeed);
    }

    @GameTest(template = "empty_9x12x9")
    public void breakingCornerRemovesAllPartsAndDropsOne(GameTestHelper helper) {
        BDoingBlock.placeAt(helper.getLevel(), helper.absolutePos(CENTER));

        BlockPos corner = BDoingPart.NORTH_WEST.offsetFrom(CENTER);
        helper.getLevel().destroyBlock(helper.absolutePos(corner), true);

        for (BlockPos partPos : BDoingPart.allPositions(CENTER)) {
            helper.assertBlockState(partPos, state -> state.isAir(), () -> "Expected air at " + partPos);
        }
        helper.assertItemEntityCountIs(ModItems.BDOING.get(), CENTER, 3.0, 1);
        helper.succeed();
    }

    @GameTest(template = "empty_9x12x9")
    public void recipeCraftsBDoing(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        // SIS / IPI / SIS: slime, iron, slime / iron, piston, iron / slime, iron, slime.
        CraftingInput valid = CraftingInput.of(3, 3, List.of(
            new ItemStack(Items.SLIME_BLOCK), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.SLIME_BLOCK),
            new ItemStack(Items.IRON_INGOT), new ItemStack(Items.PISTON), new ItemStack(Items.IRON_INGOT),
            new ItemStack(Items.SLIME_BLOCK), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.SLIME_BLOCK)));

        Optional<RecipeHolder<CraftingRecipe>> recipe =
            level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, valid, level);
        if (recipe.isEmpty()) {
            helper.fail("No crafting recipe matched the B-Doing pattern");
            return;
        }
        ItemStack result = recipe.get().value().assemble(valid, level.registryAccess());
        if (!result.is(ModItems.BDOING.get()) || result.getCount() != 1) {
            helper.fail("Recipe assembled to " + result + " instead of 1 B-Doing");
        }

        CraftingInput invalid = CraftingInput.of(3, 3, List.of(
            new ItemStack(Items.SLIME_BLOCK), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.SLIME_BLOCK),
            new ItemStack(Items.IRON_INGOT), new ItemStack(Items.DIRT), new ItemStack(Items.IRON_INGOT),
            new ItemStack(Items.SLIME_BLOCK), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.SLIME_BLOCK)));
        if (level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, invalid, level)
            .map(holder -> holder.value().assemble(invalid, level.registryAccess()).is(ModItems.BDOING.get()))
            .orElse(false)) {
            helper.fail("Dirt in the center slot still crafted a B-Doing");
        }
        helper.succeed();
    }

    @GameTest(template = "empty_9x12x9")
    public void pistonCannotPushBDoing(GameTestHelper helper) {
        BDoingBlock.placeAt(helper.getLevel(), helper.absolutePos(CENTER));
        helper.setBlock(new BlockPos(2, 1, 4),
            Blocks.PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, Direction.EAST));
        helper.setBlock(new BlockPos(1, 1, 4), Blocks.REDSTONE_BLOCK);

        helper.runAtTickTime(10, () -> {
            for (BDoingPart part : BDoingPart.PARTS) {
                BlockPos partPos = part.offsetFrom(CENTER);
                helper.assertBlockState(partPos,
                    state -> state.is(ModBlocks.BDOING.get()) && state.getValue(BDoingBlock.PART) == part,
                    () -> "Part " + part + " moved or changed at " + partPos);
            }
            helper.assertBlockState(new BlockPos(3, 1, 4),
                state -> !state.is(Blocks.PISTON_HEAD), () -> "Piston head extended into the B-Doing");
            helper.succeed();
        });
    }

    @GameTest(template = "empty_9x12x9")
    public void explosionOnCornerDropsOne(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        BDoingBlock.placeAt(level, helper.absolutePos(CENTER));

        BlockPos cornerAbs = helper.absolutePos(BDoingPart.NORTH_WEST.offsetFrom(CENTER));
        level.explode(null, cornerAbs.getX() - 0.4, cornerAbs.getY() + 0.5, cornerAbs.getZ() - 0.4,
            1.0F, Level.ExplosionInteraction.BLOCK);

        helper.runAtTickTime(5, () -> {
            for (BlockPos partPos : BDoingPart.allPositions(CENTER)) {
                helper.assertBlockState(partPos, state -> state.isAir(), () -> "Part left at " + partPos);
            }
            helper.assertItemEntityCountIs(ModItems.BDOING.get(), CENTER, 5.0, 1);
            helper.succeed();
        });
    }

    @GameTest(template = "empty_9x12x9")
    public void placementBlockedWhenObstructed(GameTestHelper helper) {
        helper.setBlock(new BlockPos(5, 1, 4), Blocks.STONE);
        if (BDoingBlock.canPlaceAt(helper.getLevel(), helper.absolutePos(CENTER), null)) {
            helper.fail("canPlaceAt returned true with a stone obstruction at (5,1,4)");
        }

        helper.setBlock(new BlockPos(5, 1, 4), Blocks.AIR);
        if (!BDoingBlock.canPlaceAt(helper.getLevel(), helper.absolutePos(CENTER), null)) {
            helper.fail("canPlaceAt returned false for a clear area");
        }

        helper.succeed();
    }
}
