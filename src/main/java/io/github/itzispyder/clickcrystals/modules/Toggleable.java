package io.github.itzispyder.clickcrystals.modules;

public interface Toggleable {

    void setEnabled(boolean enabled);

    boolean isEnabled();

    default void toggle() {
        setEnabled(!isEnabled());
    }
}
