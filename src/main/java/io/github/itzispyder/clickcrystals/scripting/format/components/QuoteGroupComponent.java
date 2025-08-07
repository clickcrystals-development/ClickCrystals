package io.github.itzispyder.clickcrystals.scripting.format.components;

public class QuoteGroupComponent extends AbstractGroupComponent {

    public QuoteGroupComponent() {
        super("(\\\".*?\\\")");
    }
}
