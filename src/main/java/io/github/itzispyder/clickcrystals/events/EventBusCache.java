package io.github.itzispyder.clickcrystals.events;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class EventBusCache {

    private final Map<Class<? extends Listener>, ListenerCache> data;

    public EventBusCache() {
        this.data = new ConcurrentHashMap<>();
    }

    public ListenerCache lookup(Listener listener) {
        return data.computeIfAbsent(listener.getClass(), ListenerCache::new);
    }

    public static class ListenerCache {
        private final ConcurrentLinkedQueue<ListenerCacheEntry> methodEventParameterCache;

        public ListenerCache(Class<? extends Listener> listener) {
            this.methodEventParameterCache = new ConcurrentLinkedQueue<>();
            Arrays.stream(listener.getDeclaredMethods())
                    .filter(method -> method.getParameterCount() == 1
                            && method.getReturnType() == void.class
                            && method.isAnnotationPresent(EventHandler.class))
                    .sorted(Comparator.comparing(method -> ((Method) method).getAnnotation(EventHandler.class).priority()).reversed())
                    .forEach(method -> methodEventParameterCache.add(new ListenerCacheEntry(method, method.getParameterTypes()[0])));
        }

        public void forEach(Consumer<ListenerCacheEntry> action) {
            methodEventParameterCache.forEach(action);
        }
    }

    public record ListenerCacheEntry(Method method, Class<?> methodEventType) {

    }
}
