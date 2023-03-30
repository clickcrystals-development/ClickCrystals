package io.github.itzispyder.clickcrystals.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an event bus for calling and passing events
 */
public class EventBus {

    private final Map<Class<? extends Listener>, Listener> subscribedListeners;

    /**
     * Constructs a new event bus for handling events
     */
    public EventBus() {
        this.subscribedListeners = new HashMap<>();
    }

    /**
     * Subscribe a listener to the event bus
     * @param listener listener
     */
    public void subscribe(Listener listener) {
        if (listener == null) return;
        subscribedListeners.remove(listener.getClass());
        subscribedListeners.put(listener.getClass(),listener);
    }

    /**
     * Unsubscribes a listener from the event bus
     * @param listener listener
     */
    public void unsubscribe(Listener listener) {
        if (listener == null) return;
        subscribedListeners.remove(listener.getClass());
    }

    /**
     * Passing an event
     * @param event event
     * @param <E> ? extends Event
     */
    public <E extends Event> void pass(E event) {
        if (event == null) return;
        for (Listener listener : this.subscribedListeners.values()) {
            for (Method method : listener.getClass().getDeclaredMethods()) {
                if (isValid(method, event)) {
                    try {
                        method.setAccessible(true);
                        method.invoke(listener, event);
                    } catch (Exception ignore) {}
                }
            }
        }
    }

    /**
     * Tests if a method is valid for passing
     * @param method method
     * @param event event
     * @return valid
     * @param <E> ? extends Event
     */
    private <E extends Event> boolean isValid(Method method, E event) {
        if (method == null) return false;
        if (!method.isAnnotationPresent(EventHandler.class)) return false;
        if (method.getReturnType() != void.class) return false;
        if (method.getParameterCount() != 1) return false;
        return method.getParameters()[0].getType() == event.getClass();
    }

    /**
     * Returns a map of registered events by the event bus
     * @return map
     */
    public HashMap<Class<? extends Listener>, Listener> listeners() {
        return new HashMap<>(subscribedListeners);
    }
}
