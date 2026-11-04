package com.bestinslot.pacmodworld2.gametest;

import com.bestinslot.pacmodworld2.PacModWorld2;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingBlock;
import com.bestinslot.pacmodworld2.block.bdoing.BDoingPart;
import com.bestinslot.pacmodworld2.registry.ModBlocks;
import com.bestinslot.pacmodworld2.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.block.Blocks;
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
