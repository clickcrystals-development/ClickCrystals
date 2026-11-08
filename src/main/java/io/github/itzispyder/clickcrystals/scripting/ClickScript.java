package io.github.itzispyder.clickcrystals.scripting;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.runtime.TickTaskQueue;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.client.system.Config;
import io.github.itzispyder.clickcrystals.scripting.components.CommandLine;
import io.github.itzispyder.clickcrystals.scripting.exceptions.ScriptException;
import io.github.itzispyder.clickcrystals.scripting.exceptions.ScriptNotFoundException;
import io.github.itzispyder.clickcrystals.scripting.exceptions.UnknownCommandException;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class ClickScript implements Global {

    public static final AtomicReference<File> currentFile = new AtomicReference<>();
    private static final Map<String, ScriptCommand> REGISTRATION = new HashMap<>();
    private static final Map<String, String> SCRIPT_ERRORS = new java.util.concurrent.ConcurrentHashMap<>();
    public static final ClickScript DEFAULT_DISPATCHER = new ClickScript("DEFAULT DISPATCHER");
    private static final java.util.Set<ClickScript> ACTIVE_TASKS =
            java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
    private static final ThreadLocal<Integer> EXECUTION_DEPTH = ThreadLocal.withInitial(() -> 0);
    private static final int MAX_EXECUTION_DEPTH = 32;

    private final Map<String, String> functions, macros;
    private final ScriptState scriptState = new ScriptState();
    private final TickTaskQueue tickTaskQueue = new TickTaskQueue();
    private final String path;
    private final File file;

    private ClickScript(File file, String path) {
        this.file = file;
        this.path = path;
        this.functions = new HashMap<>();
        this.macros = new HashMap<>();
        currentFile.set(file);
    }

    public ClickScript(File file) {
        this(file, file.getPath());
    }

    private ClickScript(String fakePath) {
        this(null, fakePath);
    }

    public static ClickScript executeIfExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            ClickScript script = new ClickScript(file);
            script.execute();
            return script;
        }
        return null;
    }

    public static void executeDynamic(String commandLine) {
        executeDynamic(DEFAULT_DISPATCHER, commandLine);
    }

    public static void executeDynamic(ClickScript executor, String commandLine) {
        int depth = EXECUTION_DEPTH.get();
        if (depth >= MAX_EXECUTION_DEPTH)
            throw new IllegalStateException("ClickScript nesting limit exceeded (32); check recursive functions");
        EXECUTION_DEPTH.set(depth + 1);
        try {
            List<CommandLine> lines = ScriptParser.parse(ScriptParser.condenseLines(commandLine));
            dynamicExecutionHelper(executor, lines);
        } finally {
            if (depth == 0) EXECUTION_DEPTH.remove();
            else EXECUTION_DEPTH.set(depth);
        }
    }

    private static void dynamicExecutionHelper(ClickScript executor, List<CommandLine> lines) {
        for (int i = 0; i < lines.size(); i++) {
            CommandLine line = lines.get(i);
            CommandLine.WaitingState state = line.getWaitingState();

            if (state.waiting()) {
                int from = i + 1;
                int to = lines.size();
                system.tickScheduler.schedule(state.seconds(), () ->
                        mc.execute(() -> dynamicExecutionHelper(executor,
                                ArrayUtils.subList(lines, from, to))));
                return;
            }

            if (line.isDeep()) {
                line.executeDynamic(executor);
            }
            else {
                line.execute(executor);
            }
        }
    }

    public static void executeSingle(String commandLine) {
        executeSingle(DEFAULT_DISPATCHER, commandLine);
    }

    public static void executeSingle(ClickScript executor, String commandLine) {
        try {
            executor.executeLine(commandLine);
        }
        catch (ScriptException e) {
            executor.printErrorDetails(e, commandLine);
        }
    }

    public static void register(ScriptCommand command) {
        if (command != null) {
            REGISTRATION.put(command.getName(), command);
            Arrays.stream(command.getAliases()).forEach(alias -> REGISTRATION.put(alias, command));
        }
    }

    public void execute() {
        if (file != null) SCRIPT_ERRORS.remove(path);
        io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd.clearCurrentScriptModule();
        try {
            if (!file.exists() || !(path.endsWith(".ccs") || path.endsWith(".txt"))) {
                throw new ScriptNotFoundException(this);
            }
            String script = ScriptParser.readFile(file.getPath());
            executeDynamic(this, script);
        }
        catch (Exception ex) {
            printErrorDetails(ex, "DEFAULT-START-EXECUTION");
        }
        finally {
            io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd.clearCurrentScriptModule();
            currentFile.set(null);
        }
    }

    private synchronized void executeLine(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("//"))
            return;

        try {
            ScriptArgsReader sar = new ScriptArgs(this, line.split(" ")).getReader();
            String name = sar.next(REGISTRATION.keySet());
            ScriptCommand cmd = REGISTRATION.get(name);

            if (cmd != null) {
                for (Map.Entry<String, String> entry : macros.entrySet())
                    line = line.replace(entry.getKey(), entry.getValue());
                cmd.dispatch(this, name, sar.getCurrentRead(), line);
            }
            else {
                printErrorDetails(new UnknownCommandException("No such command found"), line);
            }
        }
        catch (Exception ex) {
            throw new ScriptException(ex, this, line);
        }
    }

    public void printErrorDetails(Exception ex, String cmd) {
        if (file != null)
            SCRIPT_ERRORS.put(path, ex.getClass().getSimpleName() + ": " + String.valueOf(ex.getMessage()));
        String error = getErrorDetails(ex, cmd);
        if (PlayerUtils.invalid() || ClickCrystals.config.isDisableScriptChatErrors()) {
            system.printErr(error);
        }
        else {
            mc.execute(() -> Command.error(error));
        }
    }

    public static String getScriptError(String filepath) {
        return filepath == null ? null : SCRIPT_ERRORS.get(filepath);
    }

    public String getErrorDetails(Exception ex, String cmd) {
        String from = ex.getClass().getPackageName();
        String type = ex.getClass().getSimpleName();
        String msg = ex.getMessage();
        String name = cmd.split(" ")[0];

        return """
        \nError found in ClickScript command execution:
            from: %s
            type: %s
            message: '%s'
                        
            execution-details:
            -name: '%s'
            -command: '%s'
            -location: [at '%s']
        """.formatted(from, type, msg, name, cmd, path);
    }

    public void createFunction(String name, String command) {
        if (name == null || name.trim().isEmpty() || command == null || command.trim().isEmpty()) {
            return;
        }
        functions.put(name, command);
    }

    public String getFunction(String name) {
        if (name == null || name.trim().isEmpty() || !functions.containsKey(name)) {
            throw new IllegalArgumentException("Function '%s' is not defined!".formatted(name));
        }
        return "execute " + functions.getOrDefault(name, "");
    }

    public void createMacro(String name, String macro) {
        if (name == null || name.trim().isEmpty() || macro == null || macro.trim().isEmpty()) {
            return;
        }
        macros.put(name, macro);
    }

    public String getMacro(String name) {
        if (name == null || name.trim().isEmpty() || !macros.containsKey(name)) {
            throw new IllegalArgumentException("Macro '%s' is not defined!".formatted(name));
        }
        return macros.getOrDefault(name, "");
    }

    public ScriptState getScriptState() {
        return scriptState;
    }

    /**
     * Schedule a named continuation on the Minecraft game thread.
     * Task callbacks are processed only during END_CLIENT_TICK; no async game access.
     */
    public void scheduleTask(String name, int delayTicks, int repeatTicks, String continuation) {
        if (continuation == null || continuation.isBlank())
            throw new IllegalArgumentException("Task command cannot be empty");
        scheduleCallbackTask(name, delayTicks, repeatTicks,
                () -> ClickScript.executeDynamic(this, continuation));
    }

    /** Java integration point for cancellable game-thread state-machine callbacks. */
    public void scheduleCallbackTask(String name, int delayTicks, int repeatTicks, Runnable action) {
        if (action == null) throw new IllegalArgumentException("Task callback is null");
        mc.execute(() -> {
            tickTaskQueue.schedule(name, delayTicks, repeatTicks, action);
            ACTIVE_TASKS.add(this);
        });
    }

    public void cancelTask(String name) {
        mc.execute(() -> {
            tickTaskQueue.cancel(name);
            if (tickTaskQueue.size() == 0) ACTIVE_TASKS.remove(this);
        });
    }

    public void cancelTasks() {
        mc.execute(() -> {
            tickTaskQueue.cancelAll();
            ACTIVE_TASKS.remove(this);
        });
    }

    public int activeTaskCount() {
        return tickTaskQueue.size();
    }

    public static void cancelScheduledTasksForFile(String path) {
        if (path == null) return;
        mc.execute(() -> {
            for (ClickScript script : new ArrayList<>(ACTIVE_TASKS)) {
                if (script.file != null && path.equals(script.file.getPath())) {
                    script.tickTaskQueue.cancelAll();
                    ACTIVE_TASKS.remove(script);
                }
            }
        });
    }

    public static void cancelAllScheduledTasks() {
        for (ClickScript script : new ArrayList<>(ACTIVE_TASKS))
            script.tickTaskQueue.cancelAll();
        ACTIVE_TASKS.clear();
    }

    public static void tickScheduledTasks() {
        for (ClickScript script : new ArrayList<>(ACTIVE_TASKS)) {
            script.tickTaskQueue.tick();
            if (script.tickTaskQueue.size() == 0) ACTIVE_TASKS.remove(script);
        }
    }


    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public static String[] collectNames() {
        String[] a = new String[REGISTRATION.size()];
        int i = 0;
        for (String s : REGISTRATION.keySet()) {
            a[i++] = s;
        }
        return a;
    }

    public static List<File> collectFiles() {
        File folder = new File(Config.PATH_SCRIPTS);
        Function<File, List<File>> getSubFiles = file -> {
            if (!file.isDirectory()) {
                return new ArrayList<>();
            }
            File[] subs = file.listFiles();
            return subs == null ? new ArrayList<>() : Arrays.asList(subs);
        };
        List<File> files = new ArrayList<>();

        if (FileValidationUtils.validate(folder)) {
            File[] subs = folder.listFiles();
            if (subs == null) {
                return files;
            }

            for (File sub : subs) {
                if (sub.isDirectory()) {
                    files.addAll(getSubFiles.apply(sub));
                }
                else {
                    files.add(sub);
                }
            }
        }
        return files;
    }
}
