package io.github.itzispyder.clickcrystals.scripting.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierSelection<T> extends ArrayList<IdentifierSelection.Node> {

    // example: #sword,:diamond,#pickaxe[efficiency,what],:golden_apple
    // format: #name[tag,tag,..,tag]
    private static final Pattern PATTERN = Pattern.compile("[#:](?<name>\\w+)(\\[(?<tags>.*)\\])?");

    public static <T> IdentifierSelection<T> parse(String rawScriptInput, Class<T> type) {
        if (rawScriptInput != null && !rawScriptInput.isBlank())
            return new IdentifierSelection<>(rawScriptInput);
        return null;
    }

    private Function<Node, Predicate<T>> containsStrategy, equalsStrategy, defaultStrategy, tagsStrategy;
    private final boolean negated;

    private IdentifierSelection(String rawScriptInput) {
        super();
        containsStrategy = equalsStrategy = defaultStrategy = node -> object -> false;
        tagsStrategy = node -> object -> true;
        negated = rawScriptInput.charAt(0) == '!';

        Matcher matcher = PATTERN.matcher(rawScriptInput);
        while (matcher.find())
            this.add(new Node(matcher.group()));
    }

    public List<Predicate<T>> getRawIdentifierPredicates() {
        List<Predicate<T>> predicates = new ArrayList<>();
        for (Node node : this) {
            switch (node.type) {
                case CONTAINS -> predicates.add(bind(node, containsStrategy, tagsStrategy));
                case EQUALS -> predicates.add(bind(node, equalsStrategy, tagsStrategy));
                default -> predicates.add(defaultStrategy.apply(node));
            }
        }
        return predicates;
    }

    private Predicate<T> bind(Node supplier, Function<Node, Predicate<T>> a, Function<Node, Predicate<T>> b) {
        return object -> a.apply(supplier).test(object) && b.apply(supplier).test(object);
    }

    public Predicate<T> getIdentifierPredicate() {
        return negated ? getInverseIdentifierPredicate() : getNormalIdentifierPredicate();
    }

    public Predicate<T> getNormalIdentifierPredicate() {
        return object -> this.getRawIdentifierPredicates().stream().anyMatch(pre -> pre.test(object));
    }

    public Predicate<T> getInverseIdentifierPredicate() {
        return object -> this.getRawIdentifierPredicates().stream().noneMatch(pre -> pre.test(object));
    }

    public void setContainsStrategy(Function<Node, Predicate<T>> containsStrategy) {
        this.containsStrategy = containsStrategy;
    }

    public void setEqualsStrategy(Function<Node, Predicate<T>> equalsStrategy) {
        this.equalsStrategy = equalsStrategy;
    }

    public void setDefaultStrategy(Function<Node, Predicate<T>> defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    public void setNodeTagsStrategy(Function<Node, Predicate<T>> tagsStrategy) {
        this.tagsStrategy = tagsStrategy;
    }

    public static class Node {
        public final String name;
        public final List<String> tags;
        public final NodeType type;

        public Node(String rawScriptInput) {
            Matcher matcher = PATTERN.matcher(rawScriptInput);
            if (!matcher.matches())
                throw new IllegalArgumentException("malformed identifier selection");

            String name = matcher.group("name");
            String tags = matcher.group("tags");

            if (name == null)
                throw new IllegalArgumentException("identifier selection node needs a name!");

            this.name = matcher.group("name");
            this.tags = new ArrayList<>();
            this.type = NodeType.fromScript(rawScriptInput);

            if (tags != null)
                this.tags.addAll(Arrays.asList(tags.split(",")));
        }
    }

    public enum NodeType {
        EQUALS,
        CONTAINS;

        private static NodeType fromScript(String rawScriptInput) {
            switch (rawScriptInput.substring(0, 1)) {
                case ":" -> {
                    return EQUALS;
                }
                case "#" -> {
                    return CONTAINS;
                }
                default -> {
                    return null;
                }
            }
        }
    }
}
