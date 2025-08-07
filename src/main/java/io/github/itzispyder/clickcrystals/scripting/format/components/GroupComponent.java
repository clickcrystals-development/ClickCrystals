package io.github.itzispyder.clickcrystals.scripting.format.components;

import java.util.regex.Pattern;

public interface GroupComponent {

    void setOptional(boolean optional);

    boolean isOptional();

    void setLeading(boolean optional);

    boolean isLeading();

    String getAcceptingRegex();

    default boolean accept(String input) {
        return input != null && input.matches(getAcceptingRegex());
    }

    default Pattern getAcceptingPattern() {
        return Pattern.compile(getAcceptingRegex());
    }
}
