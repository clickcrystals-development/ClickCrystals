package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;

import java.util.function.Consumer;

public class ModuleCmd extends ScriptCommand {

    private static ScriptedModule currentScriptModule;

    public static ScriptedModule getCurrentScriptModule() {
        return currentScriptModule;
    }

    public static void runOnCurrentScriptModule(Consumer<ScriptedModule> action) {
        if (currentScriptModule != null) {
            action.accept(currentScriptModule);
        }
    }

    public ModuleCmd(){
        super("module");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        currentScriptModule = new ScriptedModule(args.get(0).stringValue(), "");
        Global.system.addModule(currentScriptModule);
    }
}
