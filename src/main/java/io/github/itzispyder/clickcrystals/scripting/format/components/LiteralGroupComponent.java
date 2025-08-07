package io.github.itzispyder.clickcrystals.scripting.format.components;

public class LiteralGroupComponent extends AbstractGroupComponent {

    public LiteralGroupComponent(String literal) {
        super("(" + literal + ")");
    }

    public LiteralGroupComponent() {
        super("(\\w+)");
    }
}
