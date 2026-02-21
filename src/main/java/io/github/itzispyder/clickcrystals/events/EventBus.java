package io.github.itzispyder.clickcrystals.events;

import io.github.itzispyder.clickcrystals.Global;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class EventBus implements Global {

    private final ConcurrentLinkedQueue<Listener> subscribedListeners;
    private final EventBusCache listenersCache;

    public EventBus() {
        this.subscribedListeners = new ConcurrentLinkedQueue<>();
        this.listenersCache = new EventBusCache();
    }

    public void subscribe(Listener listener) {
        if (listener == null || subscribedListeners.contains(listener))
            return;
        subscribedListeners.add(listener);
    }

    public void unsubscribe(Listener listener) {
        subscribedListeners.remove(listener);
    }

    /**
     * Passes an event
     * @param event event
     * @return true if cancelled, false if not
     * @param <E> event type
     */
    public <E extends Event> boolean pass(E event) {
        subscribedListeners.forEach(listener -> {
            listenersCache.lookup(listener)
                    .forEach(method -> this.tryInvoke(method, listener, event));
        });

        return event instanceof Cancellable c && c.isCancelled();
    }

    public <C extends CallbackInfo, E extends Event> void passWithCallbackInfo(C info, E event, Consumer<CallbackInfo> action) {
        pass(event);
        if (event instanceof Cancellable c && c.isCancelled()) {
            action.accept(info);
        }
    }

    public <C extends CallbackInfo, E extends Event> void passWithCallbackInfo(C info, E event) {
        passWithCallbackInfo(info, event, CallbackInfo::cancel);
    }

    private <E extends Event> void tryInvoke(EventBusCache.ListenerCacheEntry methodCache, Listener listener, E event) {
        Method method = methodCache.method();
        try {
            if (!isValid(methodCache, event)) return;
            method.setAccessible(true);
            method.invoke(listener, event);
        }
        catch (Exception ex) {
            system.printErr("EventBus dropped a passenger XD");
            system.printErrF("method=%s, listener=%s, for event emitted %s",
                    method.getName(),
                    listener.getClass().getSimpleName(),
                    event.getClass().getSimpleName()
            );
        }
    }

    private <E extends Event> boolean isValid(EventBusCache.ListenerCacheEntry methodCache, E event) {
        return event != null && methodCache.methodEventType() == event.getClass();
    }

    public Set<Listener> listeners() {
        return new HashSet<>(subscribedListeners);
    }
}
