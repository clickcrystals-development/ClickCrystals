import io.github.itzispyder.clickcrystals.scripting.runtime.InputLeaseManager;

public final class InputLeaseManagerTest {
    private static void check(boolean ok, String reason) {
        if (!ok) throw new AssertionError(reason);
    }

    public static void main(String[] args) {
        InputLeaseManager holds = new InputLeaseManager();
        long oldAttack = holds.acquireButton(6);
        long oldUse = holds.acquireButton(7);
        check(holds.ownsButton(6, oldAttack), "attack must initially own its lease");
        check(holds.ownsButton(7, oldUse), "use must initially own its lease");
        long[] cancelled = holds.cancelAllButtons();
        check(!holds.ownsButton(6, oldAttack), "cancel must invalidate delayed attack release");
        check(!holds.ownsButton(7, oldUse), "cancel must invalidate delayed use release");
        long newAttack = holds.acquireButton(6);
        check(!holds.ownsButton(6, cancelled[6]), "queued cancel release must not touch newer attack");
        check(holds.ownsButton(6, newAttack), "new attack must stay active");
        check(holds.ownsButton(7, cancelled[7]), "independent use ticket must remain cancellable");

        long firstKey = holds.acquireKey(88);
        check(firstKey > 0, "first physical key lease rejected");
        check(holds.acquireKey(88) == -1, "duplicate key lease should be rejected");
        holds.cancelAllKeys();
        long secondKey = holds.acquireKey(88);
        check(secondKey > firstKey, "key tickets must not repeat after cancel");
        check(!holds.releaseKey(88, firstKey), "stale delayed key release destroyed new hold");
        check(holds.activeKeyCount() == 1, "new key lease was unexpectedly removed");
        check(holds.releaseKey(88, secondKey), "current key should release");
        check(holds.activeKeyCount() == 0, "lease leaked");

        boolean invalid = false;
        try { holds.acquireButton(8); }
        catch (IllegalArgumentException expected) { invalid = true; }
        check(invalid, "invalid index accepted");
        System.out.println("InputLeaseManagerTest: PASS");
    }
}
