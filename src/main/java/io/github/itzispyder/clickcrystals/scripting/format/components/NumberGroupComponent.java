package io.github.itzispyder.clickcrystals.scripting.format.components;

public class NumberGroupComponent extends AbstractGroupComponent {

    public NumberGroupComponent() {
        super("(-?\\d*(\\.\\d*)?)");
    }
}
