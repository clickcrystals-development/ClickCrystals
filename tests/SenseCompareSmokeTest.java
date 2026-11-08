import io.github.itzispyder.clickcrystals.scripting.SenseCompare;
public final class SenseCompareSmokeTest {
    private static void ok(boolean x) { if (!x) throw new AssertionError(); }
    public static void main(String[] args) {
        ok(SenseCompare.check("0.88", "ge", "0.848"));
        ok(!SenseCompare.check("0.84", "ge", "0.848"));
        ok(SenseCompare.check("true", "true", ""));
        ok(!SenseCompare.check("false", "true", ""));
        ok(SenseCompare.check("minecraft:respawn_anchor", "contains", "anchor"));
        ok(!SenseCompare.check(null, "ne", "test"));
        ok(SenseCompare.isUnary("true"));
        ok(!SenseCompare.isUnary("ge"));
        boolean thrown=false;
        try { SenseCompare.check("NaN", "gt", "0"); }
        catch (IllegalArgumentException ex) { thrown=true; }
        ok(thrown);
        System.out.println("SenseCompareSmokeTest PASS");
    }
}
