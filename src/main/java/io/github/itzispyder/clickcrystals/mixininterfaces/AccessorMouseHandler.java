package io.github.itzispyder.clickcrystals.mixininterfaces;

public interface AccessorMouseHandler {

    void leftClick();

    void clickCrystals$rightClick();

    void clickCrystals$middleClick();

    void scroll(double amount);

    void setCursorPos(double x, double y);
}
