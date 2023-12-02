package io.github.itzispyder.clickcrystals.gui.misc;

public enum Gray {
    DARK_GRAY(0xFF222222),
    GRAY(0xFF555555),
    LIGHT_GRAY(0xFF888888),
    LIGHT(0xFFFFFFFF),
    GENERIC(0xFF73D4FF),
    GENERIC_LOW(0xFF3873A9),
    BLACK(0xFF000000);

    public final int argb;

    Gray(int argb) {
        this.argb = argb;
    }
}
