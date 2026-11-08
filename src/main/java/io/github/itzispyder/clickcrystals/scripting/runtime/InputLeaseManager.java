package io.github.itzispyder.clickcrystals.scripting.runtime;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Thread-safe generation tickets for simulated button holds.
 *
 * Delayed callbacks must verify their ticket before releasing a key:
 * an old timeout must not release a newer hold that was acquired after a
 * cancellation, screen change or reconnect.
 *
 * This is a lease primitive, not arbitration with third-party mods or the
 * physical keyboard. Script-level ownership remains a separate concern.
 */
public final class InputLeaseManager {
    public static final int BUTTON_COUNT = 8;
    private final AtomicLongArray buttonGenerations = new AtomicLongArray(BUTTON_COUNT);
    private final AtomicLong nextKeyTicket = new AtomicLong();
    private final ConcurrentHashMap<Integer, Long> keyLeases = new ConcurrentHashMap<>();

    private static void checkButton(int button) {
        if (button < 0 || button >= BUTTON_COUNT)
            throw new IllegalArgumentException("Invalid simulated button index");
    }

    public long acquireButton(int button) {
        checkButton(button);
        return buttonGenerations.incrementAndGet(button);
    }

    public boolean ownsButton(int button, long ticket) {
        checkButton(button);
        return ticket > 0 && buttonGenerations.get(button) == ticket;
    }

    public long[] cancelAllButtons() {
        long[] invalidated = new long[BUTTON_COUNT];
        for (int i = 0; i < BUTTON_COUNT; i++)
            invalidated[i] = buttonGenerations.incrementAndGet(i);
        return invalidated;
    }

    /** Returns a positive ticket, or -1 if the same key is already held. */
    public long acquireKey(int keyCode) {
        long ticket = nextKeyTicket.incrementAndGet();
        return keyLeases.putIfAbsent(keyCode, ticket) == null ? ticket : -1;
    }

    public boolean releaseKey(int keyCode, long ticket) {
        return ticket > 0 && keyLeases.remove(keyCode, ticket);
    }

    public void cancelAllKeys() {
        keyLeases.clear();
    }

    public int activeKeyCount() {
        return keyLeases.size();
    }
}
