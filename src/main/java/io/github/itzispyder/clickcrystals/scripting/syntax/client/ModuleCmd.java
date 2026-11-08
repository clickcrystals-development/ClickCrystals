package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

import java.util.function.Consumer;

// @Format module (create|enable|disable) ...
public class ModuleCmd extends ScriptCommand implements Global {

    // Module definitions are scoped to the script-loading thread, not shared
    // across other scripts or client callback threads.
    private static final ThreadLocal<ScriptedModule> currentScriptModule = new ThreadLocal<>();

    public static ScriptedModule getCurrentScriptModule() {
        return currentScriptModule.get();
    }

    public static void clearCurrentScriptModule() {
        currentScriptModule.remove();
    }

    public static void runOnCurrentScriptModule(Consumer<ScriptedModule> action) {
        ScriptedModule module = currentScriptModule.get();
        if (module != null) action.accept(module);
    }

    public ModuleCmd(){
        super("module");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var read = args.getReader();

        switch (read.next(Action.class)) {
            case CREATE -> {
                // Bind module ownership to the actual script, never a global
                // mutable file pointer shared with unrelated scripts.
                java.io.File scriptFile = args.getExecutorOrDef().getFile();
                if (scriptFile == null)
                    throw new IllegalStateException("Module cannot be created outside a script file");

                ScriptedModule created = new ScriptedModule(read.nextStr(), "", scriptFile);

                if (system.getModuleById(created.getId()) != null)
                    throw new IllegalStateException("Cannot create module '%s' because one with the same name already exists!".formatted(created.getId()));

                currentScriptModule.set(created);
                system.addModule(created);
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
