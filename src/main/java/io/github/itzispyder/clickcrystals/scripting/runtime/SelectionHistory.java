package io.github.itzispyder.clickcrystals.scripting.runtime;

import java.lang.ref.WeakReference;

/**
 * Per-script selection history for CCS "switch back".
 *
 * Restoration is allowed only when both the world session and the currently
 * selected slot still match the original scripted switch. A manual user
 * selection invalidates the pending return rather than being overwritten.
 */
public final class SelectionHistory {
    private int previous = -1;
    private int expected = -1;
    private WeakReference<Object> worldIdentity;

    public synchronized void remember(int from, int to, Object world) {
        if (from < 0 || from > 8 || to < 0 || to > 8 || world == null)
            throw new IllegalArgumentException("Invalid selection history");
        previous = from;
        expected = to;
        worldIdentity = new WeakReference<>(world);
    }

    /** Returns -1 if restoration is stale or would override manual input. */
    public synchronized int consumeReturn(int selected, Object world) {
        int result = (world != null && worldIdentity != null && world == worldIdentity.get() && selected == expected)
                ? previous : -1;
        clear();
        return result;
    }

    public synchronized void clear() {
        previous = -1;
        expected = -1;
        worldIdentity = null;
    }

    public synchronized boolean isPending() {
        return previous >= 0;
    }
}
