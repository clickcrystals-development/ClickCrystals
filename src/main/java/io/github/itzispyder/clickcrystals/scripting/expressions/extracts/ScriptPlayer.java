package io.github.itzispyder.clickcrystals.scripting.expressions.extracts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.expressions.reflection.ScriptReflect;
import io.github.itzispyder.clickcrystals.scripting.expressions.reflection.ScriptReflectObject;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

public class ScriptPlayer implements ScriptReflectObject, Global {

    @ScriptReflect(name = "player.health", type = ScriptReflect.Type.VARIABLE)
    static double getHealth() {
        return PlayerUtils.player().getHealth();
    }

    @ScriptReflect(name = "player.x", type = ScriptReflect.Type.VARIABLE)
    static double getX() {
        return PlayerUtils.player().getX();
    }
    @ScriptReflect(name = "player.y", type = ScriptReflect.Type.VARIABLE)
    static double getY() {
        return PlayerUtils.player().getY();
    }

    @ScriptReflect(name = "player.z", type = ScriptReflect.Type.VARIABLE)
    static double getZ() {
        return PlayerUtils.player().getZ();
    }
}
