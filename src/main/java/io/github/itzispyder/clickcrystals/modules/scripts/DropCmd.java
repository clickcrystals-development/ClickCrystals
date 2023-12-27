package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

public class DropCmd extends ScriptCommand {

    public DropCmd() {
        super("drop");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        switch (args.get(0).toEnum(Type.class, null)) {
            case ALL -> InteractionUtils.inputDropFull();
            case ONE -> InteractionUtils.inputDrop();
        }

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }

    public enum Type {
        ONE,
        ALL
    }
}
