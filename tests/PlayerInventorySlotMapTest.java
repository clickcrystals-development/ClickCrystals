import io.github.itzispyder.clickcrystals.scripting.runtime.PlayerInventorySlotMap;
import java.util.HashSet;

public final class PlayerInventorySlotMapTest {
    public static void main(String[] args) {
        var mapped = new HashSet<Integer>();
        for (int i = 0; i < 36; i++) {
            int slot = PlayerInventorySlotMap.screenSlot(i);
            if (slot < 9 || slot > 44 || !mapped.add(slot))
                throw new AssertionError("Invalid or duplicate inventory slot: " + i);
            if (i < 9 && slot != i + 36)
                throw new AssertionError("Wrong hotbar mapping");
            if (i >= 9 && slot != i)
                throw new AssertionError("Wrong main-inventory mapping");
        }
        if (mapped.size() != 36)
            throw new AssertionError("Missing player inventory screen slots");
        for (int invalid : new int[] {-1, 36, 40, 45}) {
            boolean rejected = false;
            try { PlayerInventorySlotMap.screenSlot(invalid); }
            catch (IllegalArgumentException expected) { rejected = true; }
            if (!rejected) throw new AssertionError("Invalid inventory index accepted");
        }
        System.out.println("PlayerInventorySlotMapTest: PASS");
    }
}
