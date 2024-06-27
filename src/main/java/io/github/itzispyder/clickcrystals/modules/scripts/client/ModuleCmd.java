package io.github.itzispyder.clickcrystals.modules.scripts.client;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;

import java.util.function.Consumer;

public class ModuleCmd extends ScriptCommand implements Global {

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
        switch (args.get(0).toEnum(Action.class, null)) {
            case CREATE -> {
                if (ClickScript.currentFile.get() == null)
                    throw new IllegalStateException("Module cannot be created: current file pointer is null!");

                ScriptedModule created = new ScriptedModule(args.get(1).toString(), "", ClickScript.currentFile.get());

                if (system.getModuleById(created.getId()) != null)
                    throw new IllegalStateException("Cannot create module '%s' because one with the same name already exists!".formatted(created.getId()));

                currentScriptModule = created;
                system.addModule(currentScriptModule);
                ClickScript.currentFile.set(null);
            }
            case ENABLE -> system.runModuleById(args.get(1).toString(), m -> m.setEnabled(true, true));
            case DISABLE -> system.runModuleById(args.get(1).toString(), m -> m.setEnabled(false, true));
        }

        if (args.match(2, "then")) {
            args.executeAll(3);
        }
    }

    public enum Action {
        CREATE,
        ENABLE,
        DISABLE
    }
}
