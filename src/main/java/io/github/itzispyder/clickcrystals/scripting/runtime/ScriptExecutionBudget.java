package io.github.itzispyder.clickcrystals.scripting.runtime;

/**
 * Bounded CCS execution context shared by nested function/condition dispatch.
 *
 * Depth alone does not prevent a flat script from executing a huge number
 * of commands in one callback. The instruction budget applies to the whole
 * outermost execution, including dynamically called functions. Each new
 * independent event or scheduled callback receives a fresh budget.
 */
public final class ScriptExecutionBudget {
    public static final int MAX_DEPTH = 64;
    public static final int MAX_COMMANDS = 4096;

    private static final ThreadLocal<Frame> CURRENT = new ThreadLocal<>();

    private static final class Frame {
        int depth;
        int commands;
    }

    private ScriptExecutionBudget() { }

    public static void enter() {
        Frame frame = CURRENT.get();
        if (frame == null) {
            frame = new Frame();
            CURRENT.set(frame);
        }
        if (frame.depth >= MAX_DEPTH)
            throw new IllegalStateException("CCS nesting limit exceeded (" + MAX_DEPTH + ")");
        frame.depth++;
    }

    public static void command() {
        Frame frame = CURRENT.get();
        if (frame == null) return;
        if (++frame.commands > MAX_COMMANDS)
            throw new IllegalStateException("CCS command budget exceeded (" + MAX_COMMANDS + ")");
    }

    public static void exit() {
        Frame frame = CURRENT.get();
        if (frame == null) return;
        if (--frame.depth <= 0) CURRENT.remove();
    }

    public static int currentDepth() {
        Frame frame = CURRENT.get();
        return frame == null ? 0 : frame.depth;
    }

    public static int commandCount() {
        Frame frame = CURRENT.get();
        return frame == null ? 0 : frame.commands;
    }
}
