import io.github.itzispyder.clickcrystals.scripting.ScriptComments;

/**
 * Pure Java smoke test: no Minecraft classes, networking, or runtime required.
 * Regression for Anchor Dual ("holding") and Spear Swap ("no") parse errors.
 */
public final class ScriptCommentsSmokeTest {
    private static void expect(String input, String expected) {
        String actual = ScriptComments.stripLine(input);
        if (!expected.equals(actual))
            throw new AssertionError("Expected <" + expected + "> but got <" + actual + "> for " + input);
    }

    public static void main(String[] args) {
        expect("// B fires ONE sequence on key down; holding B never repeats it.", "");
        expect("// Requires a Lunge-enchanted spear in the hotbar; no auto-enchanting.", "");
        expect("    // Full line comment ; holding this is not a command", "");
        expect("  pvp anchor_dual // This ; is not a command", "pvp anchor_dual");
        expect("pvp lunge_swap ; // This ; is also not a command", "pvp lunge_swap ;");
        expect("send https://example.org/abc", "send https://example.org/abc");
        expect("print \"https://example.org/?a=1;b=2\"", "print \"https://example.org/?a=1;b=2\"");
        expect("def desc \"one // two; three\" // ignored ; four", "def desc \"one // two; three\"");
        expect("on key_press b {", "on key_press b {");
        expect("}", "}");
        expect("", "");
        expect(null, "");
        System.out.println("ScriptCommentsSmokeTest: OK (both PvP comment regressions covered)");
    }
}
