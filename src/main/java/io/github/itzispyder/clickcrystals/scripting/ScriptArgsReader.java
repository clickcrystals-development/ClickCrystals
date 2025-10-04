package io.github.itzispyder.clickcrystals.scripting;

import java.util.Collection;

// everything becomes case-insensitive!
public class ScriptArgsReader {

    private final ScriptArgs args;
    private int index;
    private StringBuilder read;

    public ScriptArgsReader(ScriptArgs args) {
        this.args = args;
        this.index = 0;
        this.read = new StringBuilder();
    }

    public void executeThenChain(boolean forceThenKeyword) {
        if (forceThenKeyword && !args.match(index, "then"))
            return;

        int i = index;
        if (forceThenKeyword)
            i++;
        args.executeAll(i);
    }

    public void executeThenChain() {
        executeThenChain(true);
    }

    public String getCurrentRead() {
        return read.toString().trim();
    }

    public void resetCursor() {
        index = 0;
        read = new StringBuilder();
    }

    public void zeroCursor() {
        args.zeroCursor(index);
        resetCursor();
    }

    private void jumpToEnd() {
        index = args.getSize() - 1;
        read = new StringBuilder(args.getAll().toString());
    }

    public ScriptArgs.Arg remaining() {
        ScriptArgs.Arg arg = args.getAll(index);
        jumpToEnd();
        return arg;
    }

    public String remainingStr() {
        String arg = args.getAll(index).toString();
        jumpToEnd();
        return arg;
    }

    private void markAsRead(int nextLen) {
        for (int i = 0; i < nextLen; i++)
            read.append(args.get(index + i)).append(' ');
        index += nextLen;
    }

    public ScriptArgs.Arg next() {
        ScriptArgs.Arg next = args.get(index);
        markAsRead(1);
        return next;
    }

    public String nextStr() {
        String next = args.get(index).toString();
        markAsRead(1);
        return next;
    }

    public String nextQuote() {
        String first = args.get(index).toString();
        if (!first.startsWith("\"")) { // case: abc
            String arg = args.get(index).toString();
            markAsRead(1);
            return arg.replace("\\\"", "\"");
        }
        else if (first.startsWith("\"") && first.endsWith("\"") && !first.endsWith("\\\"")) { // case: "abc"
            String arg = args.get(index).toString();
            markAsRead(1);
            return arg.substring(1, arg.length() - 1).replace("\\\"", "\"");
        }
        else { // "ab c"
            StringBuilder builder = new StringBuilder(first);
            int endIndex = index + 1;

            for (int i = index + 1; i < args.getSize(); i++) {
                String arg = args.get(i).toString();
                builder.append(' ').append(arg);
                endIndex++;

                if (arg.endsWith("\"") && !arg.endsWith("\\\""))
                    break;
            }

            markAsRead(endIndex - index);
            return builder.substring(1, builder.length() - 1).replace("\\\"", "\"");
        }
    }

    public String next(String match) {
        String[] matches = match.toLowerCase().split("[\\w_-]+");
        for (int i = 0; i < matches.length; i++)
            if (!args.match(index + i, matches[i]))
                throw new IllegalArgumentException("expected argument: %s, got: %s"
                        .formatted(matches, args.get(index)));
        markAsRead(matches.length);
        return match;
    }

    public <T extends Enum<?>> T next(Class<T> match) {
        const_loop: for (T constant: match.getEnumConstants()) {
            if (args.match(index, constant.name())) {
                markAsRead(1);
                return constant;
            }

            String[] matches = constant.name().toLowerCase().split("[\\s_-]+");
            for (int i = 0; i < matches.length; i++)
                if (!args.match(index + i, matches[i]))
                    continue const_loop;
            markAsRead(matches.length);
            return constant;
        }

        throw new IllegalArgumentException("%s is not a value of %s"
                .formatted(args.get(index), match.getSimpleName()));
    }

    public String next(Collection<String> match) {
        const_loop: for (String constant: match) {
            if (args.match(index, constant)) {
                markAsRead(1);
                return constant;
            }

            String[] matches = constant.toLowerCase().split("[\\s_-]+");
            for (int i = 0; i < matches.length; i++)
                if (!args.match(index + i, matches[i]))
                    continue const_loop;
            markAsRead(matches.length);
            return constant;
        }

        throw new IllegalArgumentException("%s is not a value of %s"
                .formatted(args.get(index), match));
    }
}
