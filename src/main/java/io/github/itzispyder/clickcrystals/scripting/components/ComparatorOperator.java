package io.github.itzispyder.clickcrystals.scripting.components;

import java.util.Objects;

public enum ComparatorOperator {

    GREATER_THAN(">"),
    LESS_THAN("<"),
    EQUAL_TO("==", "=", "", null),
    NOT_EQUAL_TO("!=", "!"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN_OR_EQUAL_TO("<=");

    private final String[] names;

    ComparatorOperator(String... names) {
        if (names.length == 0)
            throw new IllegalArgumentException("operator must have at least 1 name!");
        this.names = names;
    }

    @Override
    public String toString() {
        return names[0];
    }

    public static ComparatorOperator parseOperator(String name) {
        for (ComparatorOperator op: ComparatorOperator.values())
            for (String tk: op.names)
                if (Objects.equals(tk, name))
                    return op;
        return null;
    }

    /**
     * Evaluates a simple script boolean
     * @param a number value
     * @param b script token consisting of operator + second number value
     * @return evaluation result
     */
    public static boolean eval(double a, String b) {
        return new ComparatorToken(b).evaluateWith(a);
    }
}
