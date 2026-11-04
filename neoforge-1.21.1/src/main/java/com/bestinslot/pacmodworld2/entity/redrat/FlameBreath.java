package com.bestinslot.pacmodworld2.entity.redrat;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class FlameBreath {
    public static final double RANGE = 4.0;
    private static final double CONE_COS = Math.cos(Math.toRadians(15.0));

    private FlameBreath() {
    }

    public static void breathe(RedRatEntity rat) {
        Level level = rat.level();
        if (level.isClientSide) {
            return;
        }
        Vec3 eye = rat.getEyePosition();
        Vec3 look = rat.getLookAngle().normalize();
        AABB search = rat.getBoundingBox().inflate(RANGE);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, search,
            entity -> entity != rat && !(entity instanceof RedRatEntity))) {
            Vec3 toTarget = target.getBoundingBox().getCenter().subtract(eye);
            double distance = toTarget.length();
            if (distance > RANGE) {
                continue;
            }
            if (distance > 0.0 && toTarget.normalize().dot(look) < CONE_COS) {
                continue;
            }
            if (!rat.hasLineOfSight(target)) {
                continue;
            }
            target.hurt(rat.damageSources().mobAttack(rat), (float) rat.getAttributeValue(Attributes.ATTACK_DAMAGE));
            target.igniteForSeconds(3.0F);
        }
        level.playSound(null, rat, SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 1.0F, 1.0F);
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 12; i++) {
                double distance = 0.5 + (RANGE - 0.5) * i / 11.0;
                Vec3 point = eye.add(look.scale(distance));
                serverLevel.sendParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 1, 0.05, 0.05, 0.05, 0.0);
            }
        }
    }
}
