package io.github.itzispyder.clickcrystals.gui_beta.misc;

public enum Gray {
    DARK_GRAY(0xFF222222),
    GRAY(0xFF555555),
    LIGHT_GRAY(0xFF888888),
    LIGHT(0xFFFFFFFF);

    public final int argb;

    Gray(int argb) {
        this.argb = argb;
    }
}
