package io.github.itzispyder.clickcrystals.scripting.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Java extension SPI for preinstalled trusted Fabric modules.
 * No reflection, scripting-classloader, dynamic JAR downloads or arbitrary eval.
 *
 * A pathfinding or AI mod can register sensors and actions at client init.
 * The host must only invoke actions from the Minecraft game thread.
 */
public final class ScriptExtensions {
    private static final Pattern ID =
            Pattern.compile("[a-z][a-z0-9_.-]{1,31}:[a-z][a-z0-9_.-]{0,63}");
    private static final Map<String, Sensor> SENSORS = new HashMap<>();
    private static final Map<String, Action> ACTIONS = new HashMap<>();
    private static final int MAX_REGISTRATIONS = 256;

    private ScriptExtensions() { }

    /** Restricted data interface; native code may provide additional adapters. */
    public interface Context {
        String readGame(String metric);
        String readState(String key);
        void writeState(String key, String value);
    }

    @FunctionalInterface
    public interface Sensor {
        String read(Context context);
    }

    @FunctionalInterface
    public interface Action {
        void execute(Context context, List<String> arguments);
    }

    private static void validate(String id) {
        if (id == null || !ID.matcher(id).matches())
            throw new IllegalArgumentException("Expected namespaced extension ID such as nav:goal");
    }

    public static synchronized void registerSensor(String id, Sensor sensor) {
        validate(id);
        if (sensor == null) throw new IllegalArgumentException("Missing extension sensor");
        if (!SENSORS.containsKey(id) && SENSORS.size() >= MAX_REGISTRATIONS)
            throw new IllegalStateException("Too many registered extension sensors");
        if (SENSORS.putIfAbsent(id, sensor) != null)
            throw new IllegalStateException("Duplicate extension sensor " + id);
    }

    public static synchronized void registerAction(String id, Action action) {
        validate(id);
        if (action == null) throw new IllegalArgumentException("Missing extension action");
        if (!ACTIONS.containsKey(id) && ACTIONS.size() >= MAX_REGISTRATIONS)
            throw new IllegalStateException("Too many registered extension actions");
        if (ACTIONS.putIfAbsent(id, action) != null)
            throw new IllegalStateException("Duplicate extension action " + id);
    }

    public static synchronized boolean has(String id) {
        validate(id);
        return ACTIONS.containsKey(id) || SENSORS.containsKey(id);
    }

    public static String read(String id, Context context) {
        Sensor sensor;
        synchronized (ScriptExtensions.class) {
            validate(id);
            sensor=SENSORS.get(id);
        }
        if (sensor == null) throw new IllegalArgumentException("Unknown extension sensor " + id);
        return sensor.read(context);
    }

    public static void call(String id, Context context, List<String> arguments) {
        Action action;
        synchronized (ScriptExtensions.class) {
            validate(id);
            action=ACTIONS.get(id);
        }
        if (action == null) throw new IllegalArgumentException("Unknown extension action " + id);
        if (arguments == null || arguments.size() > 16)
            throw new IllegalArgumentException("Too many extension arguments (max 16)");
        action.execute(context, List.copyOf(arguments));
    }

    public static synchronized int sensorCount() { return SENSORS.size(); }
    public static synchronized int actionCount() { return ACTIONS.size(); }
}
