package com.bestinslot.pacmodworld2.entity.redrat;

import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FlameBreathGoal extends Goal {
    // ~2.5s from one breath start to the next: 20-tick wind-up window plus the remaining cooldown.
    private static final int ATTACK_INTERVAL_TICKS = 50;
    private static final int BREATH_WINDOW_TICKS = 20;
    private static final int BREATH_HIT_TICK = 10;
    private static final double ATTACK_RANGE = 3.5;

    private final RedRatEntity rat;
    private int cooldown;
    private int breathTicks = -1;

    public FlameBreathGoal(RedRatEntity rat) {
        this.rat = rat;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.rat.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void stop() {
        this.breathTicks = -1;
    }

    @Override
    public void tick() {
        LivingEntity target = this.rat.getTarget();
        if (target == null) {
            return;
        }
        this.rat.getLookControl().setLookAt(target, 30.0F, 30.0F);

        double distance = this.rat.distanceTo(target);
        boolean inRange = distance <= ATTACK_RANGE && this.rat.hasLineOfSight(target);
        if (inRange) {
            this.rat.getNavigation().stop();
        } else {
            this.rat.getNavigation().moveTo(target, 1.0);
        }

        if (inRange || this.breathTicks >= 0) {
            double dx = target.getX() - this.rat.getX();
            double dz = target.getZ() - this.rat.getZ();
            float yaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            this.rat.setYRot(yaw);
            this.rat.setYBodyRot(yaw);
            this.rat.setYHeadRot(yaw);
        }

        if (this.cooldown > 0) {
            this.cooldown--;
        }

        if (this.breathTicks >= 0) {
            this.breathTicks++;
            this.spawnBreathParticles(target);
            if (this.breathTicks == BREATH_HIT_TICK) {
                FlameBreath.breathe(this.rat, aimAt(target));
            }
            if (this.breathTicks >= BREATH_WINDOW_TICKS) {
                this.breathTicks = -1;
                this.cooldown = ATTACK_INTERVAL_TICKS - BREATH_WINDOW_TICKS;
            }
        } else if (inRange && this.cooldown == 0) {
            this.rat.triggerAnim("attack", "breathfire");
            this.breathTicks = 0;
        }
    }

    private Vec3 aimAt(LivingEntity target) {
        return target.getBoundingBox().getCenter().subtract(this.rat.getEyePosition()).normalize();
    }

    private void spawnBreathParticles(LivingEntity target) {
        if (this.rat.level() instanceof ServerLevel serverLevel) {
            Vec3 eye = this.rat.getEyePosition();
            Vec3 aim = aimAt(target);
            for (int i = 0; i < 2; i++) {
                double distance = 0.5 + this.rat.getRandom().nextDouble() * 3.5;
                Vec3 point = eye.add(aim.scale(distance));
                serverLevel.sendParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 1, 0.1, 0.1, 0.1, 0.0);
            }
        }
    }
}
