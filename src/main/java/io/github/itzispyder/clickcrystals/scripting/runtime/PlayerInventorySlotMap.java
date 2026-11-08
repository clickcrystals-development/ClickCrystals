package io.github.itzispyder.clickcrystals.scripting.runtime;

/**
 * Slot translation for the vanilla player inventory ScreenHandler.
 *
 * This mapping MUST NOT be applied to chests, crafting tables, villagers,
 * or other handlers with different slot layouts.
 */
public final class PlayerInventorySlotMap {
    public static final int HOTBAR_FIRST = 36;
    public static final int HOTBAR_LAST = 44;
    public static final int OFFHAND = 45;

    private PlayerInventorySlotMap() { }

    /** PlayerInventory main-stack index (0..35) to player-screen slot (9..44). */
    public static int screenSlot(int inventoryIndex) {
        if (inventoryIndex < 0 || inventoryIndex >= 36)
            throw new IllegalArgumentException("Expected player main-inventory index 0..35");
        return inventoryIndex < 9 ? inventoryIndex + HOTBAR_FIRST : inventoryIndex;
    }
}
