package com.bestinslot.pacmodworld2.block.bdoing;

import com.bestinslot.pacmodworld2.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BDoingBlock extends BaseEntityBlock {
    public static final MapCodec<BDoingBlock> CODEC = simpleCodec(BDoingBlock::new);
    public static final EnumProperty<BDoingPart> PART = EnumProperty.create("part", BDoingPart.class);
    // ~6 block apex under real LivingEntity physics (per tick: v -= 0.08; y += v; v *= 0.98; plus a same-tick *0.98 on the set value).
    public static final double BOUNCE_VELOCITY = 1.13;
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    public BDoingBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(PART, BDoingPart.CENTER));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return state.getValue(PART) == BDoingPart.CENTER ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == BDoingPart.CENTER ? new BDoingBlockEntity(pos, state) : null;
    }

    public static boolean canPlaceAt(LevelReader level, BlockPos center, @Nullable BlockPlaceContext context) {
        for (BlockPos pos : BDoingPart.allPositions(center)) {
            if (level.isOutsideBuildHeight(pos)) {
                return false;
            }
            BlockState state = level.getBlockState(pos);
            if (context != null ? !state.canBeReplaced(context) : !state.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    public static void placeAt(Level level, BlockPos center) {
        for (BDoingPart part : BDoingPart.PARTS) {
            level.setBlock(part.offsetFrom(center), ModBlocks.BDOING.get().defaultBlockState().setValue(PART, part), Block.UPDATE_ALL);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos center = context.getClickedPos();
        return canPlaceAt(context.getLevel(), center, context)
            ? defaultBlockState().setValue(PART, BDoingPart.CENTER)
            : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            placeAt(level, pos);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && state.getValue(PART) != BDoingPart.CENTER) {
            BlockPos center = state.getValue(PART).centerPos(pos);
            BlockState centerState = level.getBlockState(center);
            if (centerState.is(this) && centerState.getValue(PART) == BDoingPart.CENTER) {
                level.setBlock(center, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, center, Block.getId(centerState));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (!newState.is(this)) {
            BlockPos center = state.getValue(PART).centerPos(pos);
            for (BlockPos partPos : BDoingPart.allPositions(center)) {
                if (partPos.equals(pos)) {
                    continue;
                }
                BlockState partState = level.getBlockState(partPos);
                if (!partState.is(this)) {
                    continue;
                }
                if (partState.getValue(PART) == BDoingPart.CENTER) {
                    level.destroyBlock(partPos, true);
                } else {
                    level.setBlock(partPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                }
            }
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        // No fall damage on the B-Doing.
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
            return;
        }
        Vec3 deltaMovement = entity.getDeltaMovement();
        if (deltaMovement.y < 0.0) {
            entity.setDeltaMovement(deltaMovement.x, BOUNCE_VELOCITY, deltaMovement.z);
            if (!entity.level().isClientSide) {
                BlockPos partPos = entity.getOnPos();
                BlockState partState = entity.level().getBlockState(partPos);
                if (partState.is(this)) {
                    BlockPos center = partState.getValue(PART).centerPos(partPos);
                    if (entity.level().getBlockEntity(center) instanceof BDoingBlockEntity blockEntity) {
                        blockEntity.triggerAnim("bounce", "bounce");
                    }
                    entity.level().playSound(null, center, SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }
}
