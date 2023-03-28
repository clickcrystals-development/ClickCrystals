package io.github.itzispyder.clickcrystals.modules;

/**
 * Toggle able interface
 */
public interface Toggleable {

    /**
     * Set toggled on or off
     * @param enabled on or off
     */
    void setEnabled(boolean enabled);

    /**
     * Check toggled on or off
     * @return on or off
     */
    boolean isEnabled();
}
