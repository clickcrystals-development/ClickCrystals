import io.github.itzispyder.clickcrystals.scripting.runtime.KeyGestureTracker;
import static io.github.itzispyder.clickcrystals.scripting.runtime.KeyGestureTracker.Signal.*;
public final class KeyGestureTrackerTest {
    private static void ok(boolean result,String msg) {if(!result)throw new AssertionError(msg);}
    public static void main(String[] args) {
        KeyGestureTracker k=new KeyGestureTracker(4,2);
        k.press();k.press(); // second press is OS auto-repeat
        ok(k.tick()==NONE,"tick 1"); ok(k.tick()==NONE,"tick 2");
        ok(k.release()==TAP,"short tap");
        k.press();for(int i=0;i<3;i++)ok(k.tick()==NONE,"hold pending");
        ok(k.tick()==HOLD_START,"hold threshold");
        ok(k.tick()==NONE,"first repeat pending");
        ok(k.tick()==HOLD_REPEAT,"repeat interval");
        ok(k.release()==NONE,"no tap following hold");
        k.press();k.tick();k.cancel();ok(k.release()==NONE,"cancel must clear key");
        System.out.println("KeyGestureTrackerTest: PASS");
    }
}
