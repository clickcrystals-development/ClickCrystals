package io.github.itzispyder.clickcrystals.client.clickscript;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandLine;
import io.github.itzispyder.clickcrystals.client.clickscript.exceptions.ScriptNotFoundException;
import io.github.itzispyder.clickcrystals.client.clickscript.exceptions.UnknownCommandException;
import io.github.itzispyder.clickcrystals.commands.Command;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ClickScript implements Global {

    public static final AtomicReference<File> currentFile = new AtomicReference<>();
    private static final Map<String, ScriptCommand> REGISTRATION = new HashMap<>() {{
        this.put("exit", ScriptCommand.create("exit", (command, line, args) -> {
            System.exit(args.get(0).toInt());
        }));
        this.put("print", ScriptCommand.create("print", (command, line, args) -> {
            system.println(args.getQuoteAndRemove());
            if (args.match(0, "then")) {
                args.executeAll(1);
            }
        }));
        this.put("throw", ScriptCommand.create("throw", (command, line, args) -> {
            throw new RuntimeException(args.getQuoteAndRemove());
        }));
        this.put("execute", ScriptCommand.create("execute", (command, line, args) -> {
            args.executeAll();
        }));
        this.put("loop", ScriptCommand.create("loop", (command, line, args) -> {
            int times = args.get(0).toInt();
            for (int i = 0; i < times; i++) {
                args.executeAll(1);
            }
        }));
    }};
    public static final ClickScript DEFAULT_DISPATCHER = new ClickScript("DEFAULT DISPATCHER");
    private final String path;
    private final File file;

    public ClickScript(File file) {
        this.file = file;
        this.path = file.getPath();
        currentFile.set(file);
    }

    private ClickScript(String fakePath) {
        this.path = fakePath;
        this.file = null;
        currentFile.set(null);
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
        for (CommandLine line : ScriptParser.getStackLines(ScriptParser.condenseLines(commandLine))) {
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
        executor.executeLine(commandLine);
    }

    public static void register(ScriptCommand command) {
        if (command != null) {
            REGISTRATION.put(command.getName(), command);
        }
    }

    public static void register(String name, ScriptCommand.Execution execution) {
        if (name != null && !name.isEmpty() && execution != null) {
            REGISTRATION.put(name, new ScriptCommand(name) {
                @Override
                public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
                    execution.execute(command, line, args);
                }
            });
        }
    }

    public static void unregister(ScriptCommand command) {
        if (command != null) {
            REGISTRATION.remove(command.getName());
        }
    }

    public void execute() {
        try {
            if (!file.exists() || !(path.endsWith(".ccs") || path.endsWith(".txt"))) {
                throw new ScriptNotFoundException(this);
            }
            String script = ScriptParser.readFile(file.getPath());
            executeDynamic(this, script);
            /*
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            br.lines().forEach(this::executeLine);
            br.close();
             */
        }
        catch (Exception ex) {
            printErrorDetails(ex, "DEFAULT-START-EXECUTION");
        }
    }

    private void executeLine(String line) {
        if (line != null && !line.trim().isEmpty() && !line.startsWith("//")) {
            String name = line.split(" ")[0];
            ScriptCommand cmd = REGISTRATION.get(name);

            if (cmd != null) {
                cmd.dispatch(this, line);
            }
            else {
                printErrorDetails(new UnknownCommandException("No such command found"), line);
            }
        }
    }

    public void printErrorDetails(Exception ex, String cmd) {
        String error = getErrorDetails(ex, cmd);
        if (mc == null || mc.world == null || mc.player == null) {
            system.printErr(error);
        }
        else {
            Command.error(error);
        }
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
}
