import io.github.itzispyder.clickcrystals.scripting.runtime.AsyncDecisionBridge;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public final class AsyncDecisionBridgeTest {
    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    private static void waitForQueue(ConcurrentLinkedQueue<Runnable> callbacks) throws Exception {
        for (int i = 0; i < 200 && callbacks.isEmpty(); i++) Thread.sleep(5);
        check(!callbacks.isEmpty(), "worker did not post callback");
    }

    public static void main(String[] args) throws Exception {
        var callbacks = new ConcurrentLinkedQueue<Runnable>();
        var result = new AtomicReference<String>();
        var error = new AtomicReference<Throwable>();

        try (var bridge = new AsyncDecisionBridge(callbacks::add)) {
            long first = bridge.submit("snapshot", s -> "plan:" + s, result::set, error::set);
            waitForQueue(callbacks);
            check(bridge.pendingCount() == 1, "pending must include queued main-thread result");
            callbacks.poll().run();
            check("plan:snapshot".equals(result.get()), "normal result not applied");
            check(error.get() == null, "unexpected compute error");
            check(bridge.pendingCount() == 0, "delivered request leaked");
            check(!bridge.cancel(first), "already delivered request should not be cancellable");

            // Regression: the old bridge removed requests on worker completion.
            // The queued main-thread apply could not then be cancelled by ID.
            long stale = bridge.submit("obsolete", s -> "BAD", result::set, error::set);
            waitForQueue(callbacks);
            check(bridge.cancel(stale), "completed but queued request must be cancellable");
            callbacks.poll().run();
            check("plan:snapshot".equals(result.get()), "cancelled result reached game state");
            check(bridge.pendingCount() == 0, "cancelled request leaked");

            bridge.submit("stale-after-reset", s -> "BAD", result::set, error::set);
            waitForQueue(callbacks);
            bridge.cancelAll();
            callbacks.poll().run();
            check("plan:snapshot".equals(result.get()), "cancelAll allowed stale result");
            check(bridge.pendingCount() == 0, "cancelAll leaked requests");

            long failed = bridge.submit("error", s -> {
                throw new IllegalStateException("expected compute failure");
            }, result::set, error::set);
            waitForQueue(callbacks);
            callbacks.poll().run();
            check(error.get() instanceof IllegalStateException, "compute failure not delivered");
            check(bridge.pendingCount() == 0, "failed request leaked");
            check(!bridge.cancel(failed), "completed failure not cancellable");

            // The capacity bound must include results that have been computed
            // but not yet applied: they still consume main-thread queue memory.
            for (int i = 0; i < AsyncDecisionBridge.MAX_PENDING; i++)
                bridge.submit(i, s -> s, s -> { }, t -> { });
            for (int i = 0; i < 200 && callbacks.size() < AsyncDecisionBridge.MAX_PENDING; i++)
                Thread.sleep(5);
            check(callbacks.size() == AsyncDecisionBridge.MAX_PENDING, "pending callbacks missing");
            check(bridge.pendingCount() == AsyncDecisionBridge.MAX_PENDING, "pending capacity not enforced");
            boolean rejected = false;
            try {
                bridge.submit("extra", s -> s, s -> { }, t -> { });
            } catch (java.util.concurrent.RejectedExecutionException ex) {
                rejected = true;
            }
            check(rejected, "pending cap should include undelivered callbacks");
            bridge.cancelAll();
            for (Runnable callback; (callback = callbacks.poll()) != null; ) callback.run();
            check(bridge.pendingCount() == 0, "cancelAll after capacity check leaked");
        }
        System.out.println("AsyncDecisionBridgeTest: PASS");
    }
}
