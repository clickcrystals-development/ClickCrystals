package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

import java.util.function.Consumer;

// @Format module (create|enable|disable) ...
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
        var read = args.getReader();

        switch (read.next(Action.class)) {
            case CREATE -> {
                if (ClickScript.currentFile.get() == null)
                    throw new IllegalStateException("Module cannot be created: current file pointer is null!");

                ScriptedModule created = new ScriptedModule(read.nextStr(), "", ClickScript.currentFile.get());

                if (system.getModuleById(created.getId()) != null)
                    throw new IllegalStateException("Cannot create module '%s' because one with the same name already exists!".formatted(created.getId()));

                currentScriptModule = created;
                system.addModule(currentScriptModule);
                ClickScript.currentFile.set(null);
            }
            case ENABLE -> system.runModuleById(read.nextStr(), m -> m.setEnabled(true, true));
            case DISABLE -> system.runModuleById(read.nextStr(), m -> m.setEnabled(false, true));
        }

        read.executeThenChain();
    }

    public enum Action {
        CREATE,
        ENABLE,
        DISABLE
    }
}
