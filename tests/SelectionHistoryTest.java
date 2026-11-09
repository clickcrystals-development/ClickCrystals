import io.github.itzispyder.clickcrystals.scripting.runtime.SelectionHistory;

public final class SelectionHistoryTest {
    private static void check(boolean b, String reason) {
        if (!b) throw new AssertionError(reason);
    }

    public static void main(String[] args) {
        Object worldA = new Object();
        Object worldB = new Object();
        SelectionHistory scriptA = new SelectionHistory();
        SelectionHistory scriptB = new SelectionHistory();
        scriptA.remember(0, 4, worldA);
        scriptB.remember(6, 2, worldA);
        check(scriptA.consumeReturn(4, worldA) == 0, "script A's return was overwritten");
        check(scriptB.consumeReturn(2, worldA) == 6, "script B's return was overwritten");
        check(!scriptA.isPending(), "consumed return must reset");

        scriptA.remember(0, 4, worldA);
        check(scriptA.consumeReturn(7, worldA) == -1, "manual selection was overwritten");
        check(scriptA.consumeReturn(4, worldA) == -1, "stale return survived manual override");

        scriptA.remember(0, 4, worldA);
        check(scriptA.consumeReturn(4, worldB) == -1, "switch back crossed world reconnect");
        scriptA.remember(2, 5, worldB);
        check(scriptA.consumeReturn(5, worldB) == 2, "new world history failed");

        scriptA.remember(0, 1, worldB);
        scriptA.clear();
        check(scriptA.consumeReturn(1, worldB) == -1, "clear did not invalidate history");
        System.out.println("SelectionHistoryTest: PASS");
    }
}
