package io.github.itzispyder.clickcrystals.util.misc;

import java.util.function.Consumer;
import java.util.function.Function;

public class Voidable<T> {

    private final T value;

    private Voidable(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void accept(Consumer<T> action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public T getOrDef(T fallback) {
        return isPresent() ? value : fallback;
    }

    public T getOrThrow() {
        assert isPresent() : "value is not present.";
        return value;
    }

    public <U> Voidable<U> map(Function<T, U> function) {
        return of(function.apply(value));
    }

    public static <T> Voidable<T> of(T value) {
        return new Voidable<>(value);
    }
}
