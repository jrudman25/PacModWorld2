package com.bestinslot.pacmodworld2.entity.redrat;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class HopMoveControl extends MoveControl {
    private static final int HOP_COOLDOWN_TICKS = 10;

    private int hopCooldown;

    public HopMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO && this.mob.onGround()) {
            if (this.hopCooldown <= 0) {
                this.mob.getJumpControl().jump();
                this.hopCooldown = HOP_COOLDOWN_TICKS;
            } else {
                this.hopCooldown--;
                this.mob.setZza(0.0F);
                this.mob.setXxa(0.0F);
                return;
            }
        }
        super.tick();
    }
}
