package io.github.itzispyder.clickcrystals.scripting.syntax.macros.camera;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
import io.github.itzispyder.clickcrystals.scripting.syntax.AimAnchorType;
import net.minecraft.util.math.Vec3d;

public class TurnOptions {

    public AimAnchorType anchor;
    public float speed;

    public TurnOptions() {
        anchor = AimAnchorType.FEET;
        speed = 10F;
    }

    public Vec3d getCameraTicketPos(Vec3d camera, Vec3d dest) {
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