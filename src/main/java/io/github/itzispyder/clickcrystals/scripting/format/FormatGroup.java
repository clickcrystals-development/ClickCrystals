package io.github.itzispyder.clickcrystals.scripting.format;

import io.github.itzispyder.clickcrystals.scripting.format.components.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormatGroup implements GroupComponent {

    private final List<GroupComponent> components;
    private boolean optional;

    private FormatGroup() {
        this.components = new ArrayList<>();
    }

    public void append(GroupComponent component) {
        components.add(component);
    }

    public int size() {
        return components.size();
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public void clear() {
        components.clear();
    }

    @Override
    public String getAcceptingRegex() {
        return "(" + String.join("", components.stream().map(GroupComponent::getAcceptingRegex).toList()) + ")" + (optional ? "?" : "");
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    @Override
    public boolean isLeading() {
        return false;
    }

    @Override
    public void setLeading(boolean optional) {

    }

    public static FormatGroupBuilder builder() {
        return new FormatGroupBuilder();
    }

    public static class FormatGroupBuilder {
        private final List<GroupComponent> components;

        public FormatGroupBuilder() {
            this.components = new ArrayList<>();
        }

        public FormatGroupBuilder thenInt(boolean optional) {
            GroupComponent component = new IntegerGroupComponent();
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public FormatGroupBuilder thenInt() {
            return thenInt(false);
        }

        public FormatGroupBuilder thenNum(boolean optional) {
            GroupComponent component = new NumberGroupComponent();
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public FormatGroupBuilder thenNum() {
            return thenNum(false);
        }

        public FormatGroupBuilder thenLiteral(String literal, boolean optional) {
            GroupComponent component = new LiteralGroupComponent(literal);
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public FormatGroupBuilder thenLiteral(String literal) {
            return thenLiteral(literal, false);
        }

        public FormatGroupBuilder thenString(boolean optional) {
            GroupComponent component = new LiteralGroupComponent();
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public FormatGroupBuilder thenString() {
            return thenString(false);
        }

        public FormatGroupBuilder thenQuotedString(boolean optional) {
            GroupComponent component = new QuoteGroupComponent();
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public FormatGroupBuilder thenQuotedString() {
            return thenQuotedString(false);
        }

        public <L extends Iterable<String>> FormatGroupBuilder thenMultiLiteral(L literals, boolean optional) {
            GroupComponent component = new MultiLiteralGroupComponent(literals);
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public <L extends Iterable<String>> FormatGroupBuilder thenMultiLiteral(L literals) {
            return thenMultiLiteral(literals, false);
        }

        public FormatGroupBuilder thenMultiLiteral(boolean optional, String... literals) {
            List<String> strings = new ArrayList<>(Arrays.asList(literals));
            GroupComponent component = new MultiLiteralGroupComponent(strings);
            component.setOptional(optional);
            addComponent(component);
            return this;
        }

        public FormatGroupBuilder thenMultiLiteral(String... literals) {
            return thenMultiLiteral(false, literals);
        }

        private void addComponent(GroupComponent component) {
            if (components.isEmpty())
                component.setLeading(true);
            components.add(component);
        }

        public FormatGroup build() {
            FormatGroup group = new FormatGroup();
            components.forEach(group::append);
            return group;
        }
    }
}
