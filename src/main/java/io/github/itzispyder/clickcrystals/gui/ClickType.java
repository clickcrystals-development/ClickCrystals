package io.github.itzispyder.clickcrystals.gui;

public enum ClickType {

    RELEASE,
    CLICK,
    UNKNOWN;

    public static ClickType of(int action) {
        ClickType r;
        switch (action) {
            case 0 -> r = RELEASE;
            case 1 -> r = CLICK;
            default -> r = UNKNOWN;
        }
        return r;
    }
}
