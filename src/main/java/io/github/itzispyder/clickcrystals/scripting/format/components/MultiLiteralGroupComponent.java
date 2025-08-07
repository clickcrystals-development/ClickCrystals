package io.github.itzispyder.clickcrystals.scripting.format.components;

public class MultiLiteralGroupComponent extends AbstractGroupComponent {

    public MultiLiteralGroupComponent(Iterable<String> literals) {
        super("(" + String.join("|", literals) + ")");
    }
}
