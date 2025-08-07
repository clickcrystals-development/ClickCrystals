package io.github.itzispyder.clickcrystals.scripting.format.components;

public abstract class AbstractGroupComponent implements GroupComponent {

    private final String acceptingRegex;
    private boolean optional, leading;

    protected AbstractGroupComponent(String acceptingRegex) {
        this.acceptingRegex = acceptingRegex;
    }

    @Override
    public String getAcceptingRegex() {
        if (leading)
            return acceptingRegex + (optional ? "?" : "");
        return "( (" + acceptingRegex + "))" + (optional ? "?" : ""); // space between each trailing argument
    }

    @Override
    public boolean isLeading() {
        return leading;
    }

    @Override
    public void setLeading(boolean leading) {
        this.leading = leading;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
