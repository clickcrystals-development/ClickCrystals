package io.github.itzispyder.clickcrystals.scripting.syntax.macros.camera;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
import io.github.itzispyder.clickcrystals.scripting.syntax.AimAnchorType;
import net.minecraft.world.phys.Vec3;

public class TurnOptions {

    public AimAnchorType anchor;
    public float speed;

    public TurnOptions() {
        anchor = AimAnchorType.FEET;
        speed = 10F;
    }

    public Vec3 getCameraTicketPos(Vec3 camera, Vec3 dest) {
        return anchor.positionFactory.apply(dest).subtract(camera).normalize();
    }

    public void configure(ScriptArgsReader read) {
        if (read.currMatches("speed")) {
            read.next(); // skips argument "speed"
            speed = read.next().toFloat();
        }
        else if (read.currMatches("aim")) {
            read.next(); // skips argument "speed"
            anchor = read.next(AimAnchorType.class);
        }
    }
}