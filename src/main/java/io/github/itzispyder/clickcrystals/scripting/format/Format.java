package io.github.itzispyder.clickcrystals.scripting.format;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {

    private final List<FormatGroup> groups;

    public Format() {
        this.groups = new ArrayList<>();
    }

    public List<String> parseGroups(String input) {
        Pattern pattern = Pattern.compile(getAcceptingRegex());
        Matcher matcher = pattern.matcher(input);
        List<String> results = new ArrayList<>();

        while (matcher.find())
            results.add(matcher.group());
        return results;
    }

    public String parseBeautify(int indents, String input) {
        List<String> groups = parseGroups(input);
        int len = groups.size();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < len; i++)
            builder.repeat("\t", indents + i).append(groups.get(i)).append(" {\n");
        for (int i = len - 1; i >= 0; i--)
            builder.repeat("\t", indents + i).append("}\n");
        return builder.toString();
    }

    public Format append(FormatGroup group) {
        groups.add(group);
        return this;
    }

    public FormatGroup.FormatGroupBuilder append() {
        return new FormatGroup.FormatGroupBuilder() {
            @Override
            public FormatGroup build() {
                FormatGroup group = super.build();
                Format.this.append(group);
                return group;
            }
        };
    }

    public int size() {
        return groups.size();
    }

    public boolean isEmpty() {
        return groups.isEmpty();
    }

    public void clear() {
        groups.clear();
    }

    public String getAcceptingRegex() {
        return "(" + String.join("|", groups.stream().map(FormatGroup::getAcceptingRegex).toList()) + ")";
    }
}
