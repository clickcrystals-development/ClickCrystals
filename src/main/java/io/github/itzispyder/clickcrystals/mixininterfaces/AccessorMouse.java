package io.github.itzispyder.clickcrystals.mixininterfaces;

public interface AccessorMouse {

    void leftClick();

    void clickCrystals$rightClick();

    void clickCrystals$middleClick();

    void scroll(double amount);

    void setCursorPos(double x, double y);
}
