package com.bestinslot.pacmodworld2.block.bdoing;

import com.bestinslot.pacmodworld2.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BDoingBlockEntity extends BlockEntity implements GeoBlockEntity {
    private static final RawAnimation BOUNCE_ANIM = RawAnimation.begin().thenPlay("bounce");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BDoingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BDOING.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "bounce", 0, state -> PlayState.STOP)
            .triggerableAnim("bounce", BOUNCE_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
