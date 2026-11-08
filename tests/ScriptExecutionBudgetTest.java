import io.github.itzispyder.clickcrystals.scripting.runtime.ScriptExecutionBudget;

public final class ScriptExecutionBudgetTest {
    private static void check(boolean valid, String message) {
        if (!valid) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        ScriptExecutionBudget.enter();
        try {
            for (int i = 0; i < 40; i++) ScriptExecutionBudget.enter();
            check(ScriptExecutionBudget.currentDepth() == 41, "depth 41 should be supported");
            for (int i = 0; i < 40; i++) ScriptExecutionBudget.exit();

            for (int i = 0; i < ScriptExecutionBudget.MAX_COMMANDS; i++)
                ScriptExecutionBudget.command();
            check(ScriptExecutionBudget.commandCount() == ScriptExecutionBudget.MAX_COMMANDS,
                    "command budget boundary incorrect");

            boolean failed = false;
            try { ScriptExecutionBudget.command(); }
            catch (IllegalStateException expected) { failed = true; }
            check(failed, "flat command overflow was not bounded");

            for (int i = 1; i < ScriptExecutionBudget.MAX_DEPTH; i++)
                ScriptExecutionBudget.enter();
            check(ScriptExecutionBudget.currentDepth() == ScriptExecutionBudget.MAX_DEPTH,
                    "maximum nesting should remain reachable");

            failed = false;
            try { ScriptExecutionBudget.enter(); }
            catch (IllegalStateException expected) { failed = true; }
            check(failed, "recursion limit was not enforced");
            for (int i = 1; i < ScriptExecutionBudget.MAX_DEPTH; i++)
                ScriptExecutionBudget.exit();
        } finally {
            ScriptExecutionBudget.exit();
        }

        check(ScriptExecutionBudget.currentDepth() == 0, "context leaked across invocations");
        ScriptExecutionBudget.enter();
        try {
            check(ScriptExecutionBudget.commandCount() == 0, "budget must reset on next event");
            ScriptExecutionBudget.command();
        } finally {
            ScriptExecutionBudget.exit();
        }
        System.out.println("ScriptExecutionBudgetTest: PASS");
    }
}
