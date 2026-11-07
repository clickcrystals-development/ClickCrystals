import io.github.itzispyder.clickcrystals.scripting.ScriptState;

/** Dependency-free regression test for state expressions and limits. */
public final class ScriptStateSmokeTest {
    private static void expect(boolean valid, String message) {
        if (!valid) throw new AssertionError(message);
    }
    private static void fails(Runnable action) {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Expected argument or state error");
    }
    public static void main(String[] args) {
        ScriptState state = new ScriptState();
        state.set("count", "1");
        state.add("count", 2);
        state.math("count", "mul", 3);
        expect(state.matches("count", "eq", "9"), "math");
        state.math("count", "sub", 1);
        state.math("count", "div", 2);
        expect(state.matches("count", "eq", "4"), "division");
        state.math("count", "mod", 3);
        state.math("count", "max", 2);
        state.clamp("count", 0, 1);
        expect(state.matches("count", "le", "1"), "clamp");
        state.set("label", "anchor");
        state.append("label", "-ready");
        state.copy("mirror", "label");
        state.length("length", "mirror");
        expect(state.matches("length", "eq", "12"), "string length");
        expect(state.matches("label", "starts", "anch"), "prefix");
        expect(state.matches("label", "ends", "ready"), "suffix");
        expect(state.matches("label", "contains", "or-r"), "contains");
        state.toggle("active");
        expect(state.matches("active", "true", ""), "toggle on");
        state.toggle("active");
        expect(state.matches("active", "false", ""), "toggle off");
        expect(state.matches("unknown", "empty", ""), "missing key");
        expect(state.acquire("hit", 5000), "first gate");
        expect(!state.acquire("hit", 5000), "duplicate gate");
        state.clear("hit");
        expect(state.acquire("hit", 5000), "clear gate");
        fails(() -> state.math("count", "div", 0));
        fails(() -> state.math("count", "add", Double.NaN));
        fails(() -> state.clamp("count", 10, 2));
        fails(() -> state.set("bad name", "x"));
        fails(() -> state.set("safe", "a".repeat(257)));
        state.reset();
        expect(state.matches("label", "empty", ""), "reset");
        System.out.println("ScriptStateSmokeTest: PASS");
    }
}
