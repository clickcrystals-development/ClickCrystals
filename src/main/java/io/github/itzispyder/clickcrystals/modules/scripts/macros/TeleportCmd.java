package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.LocationParser;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.util.math.Vec3d;

public class TeleportCmd extends ScriptCommand {

    public TeleportCmd() {
        super("teleport");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        LocationParser parser = new LocationParser(
                args.get(0).toString(),
                args.get(1).toString(),
                args.get(2).toString(),
                PlayerUtils.getPos()
        );
        Vec3d dest = parser.getLocation();
        PlayerUtils.player().setPos(dest.x, dest.y, dest.z);

        if (args.match(3, "then")) {
            args.executeAll(4);
        }
    }
}
