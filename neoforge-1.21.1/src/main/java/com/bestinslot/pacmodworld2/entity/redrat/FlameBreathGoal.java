package com.bestinslot.pacmodworld2.entity.redrat;

import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FlameBreathGoal extends Goal {
    private static final int ATTACK_COOLDOWN_TICKS = 60;
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

        if (this.cooldown > 0) {
            this.cooldown--;
        }

        if (this.breathTicks >= 0) {
            this.breathTicks++;
            this.spawnBreathParticles();
            if (this.breathTicks == BREATH_HIT_TICK) {
                FlameBreath.breathe(this.rat);
            }
            if (this.breathTicks >= BREATH_WINDOW_TICKS) {
                this.breathTicks = -1;
                this.cooldown = ATTACK_COOLDOWN_TICKS;
            }
        } else if (inRange && this.cooldown == 0) {
            this.rat.triggerAnim("attack", "breathfire");
            this.breathTicks = 0;
        }
    }

    private void spawnBreathParticles() {
        if (this.rat.level() instanceof ServerLevel serverLevel) {
            Vec3 eye = this.rat.getEyePosition();
            Vec3 look = this.rat.getLookAngle().normalize();
            for (int i = 0; i < 2; i++) {
                double distance = 0.5 + this.rat.getRandom().nextDouble() * 3.5;
                Vec3 point = eye.add(look.scale(distance));
                serverLevel.sendParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 1, 0.1, 0.1, 0.1, 0.0);
            }
        }
    }
}
