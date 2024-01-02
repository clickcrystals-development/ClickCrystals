package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.util.math.Vec3d;

public class VelocityCmd extends ScriptCommand {

    public VelocityCmd() {
        super("velocity");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        VectorParser parser = new VectorParser(
                args.get(0).toString(),
                args.get(1).toString(),
                args.get(2).toString(),
                PlayerUtils.player().getVelocity(),
                PlayerUtils.player().getRotationClient()
        );
        Vec3d dest = parser.getVector();
        PlayerUtils.player().setVelocity(dest);

        if (args.match(3, "then")) {
            args.executeAll(4);
        }
    }
}
