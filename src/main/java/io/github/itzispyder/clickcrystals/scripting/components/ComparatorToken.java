package io.github.itzispyder.clickcrystals.scripting.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// parses script tokens that look like:
// >=30 <4.0 ==5 !=2
public class ComparatorToken {

    public static final Pattern TOKEN_REGEX = Pattern.compile("(?<operator>[<>!=]=?)?(?<value>(-?\\d*(\\.\\d*)?))");
    private final ComparatorOperator operator;
    private final double value;

    public ComparatorToken(String scriptToken) {
        Matcher matcher = TOKEN_REGEX.matcher(scriptToken);

        if (!matcher.find())
            throw new IllegalArgumentException("invalid comparator script token: " + scriptToken);

        String operator = matcher.group("operator");
        String value = matcher.group("value");

        this.operator = ComparatorOperator.parseOperator(operator);
        this.value = Double.parseDouble(value);
    }

    public ComparatorOperator getOperator() {
        return operator;
    }

    public double getValue() {
        return value;
    }

    // input <this.operator> <this.value>
    public boolean evaluateWith(double input) {
        switch (operator) {
            case EQUAL_TO -> {
                return input == value;
            }
            case NOT_EQUAL_TO -> {
                return input != value;
            }
            case GREATER_THAN -> {
                return input > value;
            }
            case LESS_THAN -> {
                return input < value;
            }
            case GREATER_THAN_OR_EQUAL_TO -> {
                return input >= value;
            }
            case LESS_THAN_OR_EQUAL_TO -> {
                return input <= value;
            }
            case null, default -> {
                return false;
            }
        }
    }
}