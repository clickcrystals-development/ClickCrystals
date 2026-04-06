package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.world.phys.Vec3;

// @Format teleport <x> <y> <z>
public class TeleportCmd extends ScriptCommand {

    public TeleportCmd() {
        super("teleport");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        VectorParser parser = new VectorParser(
                args.get(0).toString(),
                args.get(1).toString(),
                args.get(2).toString(),
                PlayerUtils.player()
        );
        Vec3 dest = parser.getVector();
        PlayerUtils.player().setPosRaw(dest.x, dest.y, dest.z);

        if (args.match(3, "then")) {
            args.executeAll(4);
        }
    }
}
