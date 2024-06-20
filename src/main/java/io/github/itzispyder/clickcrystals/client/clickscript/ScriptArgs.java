package io.github.itzispyder.clickcrystals.client.clickscript;

public class ScriptArgs {

    private final ClickScript executor;
    private String[] args;

    public ScriptArgs(ClickScript executor, String... args) {
        this.executor = executor;
        this.args = args;
    }

    public ClickScript getExecutor() {
        return executor;
    }

    public ClickScript getExecutorOrDef() {
        return hasExecutor() ? getExecutor() : ClickScript.DEFAULT_DISPATCHER;
    }

    public boolean hasExecutor() {
        return executor != null;
    }

    public Arg getAll() {
        return getAll(0);
    }

    public Arg getAll(int beginIndex) {
        String str = "";
        for (int i = beginIndex; i < args.length; i++) {
            str = str.concat(args[i] + " ");
        }
        return new Arg(str.trim());
    }

    public Arg get(int index) {
        if (args.length == 0)
            throw new IllegalArgumentException("not enough arguments: arguments are empty");
        if (index < 0 || index >= args.length)
            throw new IllegalArgumentException("not enough arguments: argument %s is missing".formatted(index + 1));
        return new Arg(args[index]);
    }

    public String getQuoteAndRemove(int beginIndex) {
        String all = getAll(beginIndex).toString();
        var section = ScriptParser.firstSectionWithIndex(all, '"', '"');

        if (section.left.isEmpty()) {
            args = all.split(" ");
            return all;
        }

        args = all.substring(section.right).trim().split(" ");
        return section.left;
    }

    public String getQuoteAndRemove() {
        return getQuoteAndRemove(0);
    }

    public String getQuote(int beginIndex) {
        String all = getAll(beginIndex).toString();
        String quote = ScriptParser.firstSection(all, '"');
        return quote.isEmpty() ? all : quote;
    }

    public String getQuote() {
        return getQuote(0);
    }

    public Arg first() {
        return get(0);
    }

    public Arg last() {
        return get(args.length - 1);
    }

    public boolean match(int index, String arg) {
        if (index < 0 || index >= args.length) {
            return false;
        }
        return get(index).toString().equalsIgnoreCase(arg);
    }

    public void executeAll(int begin) {
        ClickScript.executeDynamic(getExecutorOrDef(), getAll(begin).toString());
    }

    public void executeAll() {
        executeAll(0);
    }

    public int getSize() {
        return args.length;
    }

    public boolean isEmpty() {
        return args.length == 0;
    }

    public String[] args() {
        return args;
    }



    public static class Arg {

        private final String arg;

        public Arg(String arg) {
            this.arg = arg;
        }

        public int toInt() {
            return Integer.parseInt(arg);
        }

        public long toLong() {
            return Long.parseLong(arg);
        }

        public byte toByte() {
            return Byte.parseByte(arg);
        }

        public short toShort() {
            return Short.parseShort(arg);
        }

        public double toDouble() {
            return Double.parseDouble(arg);
        }

        public float toFloat() {
            return Float.parseFloat(arg);
        }

        public boolean toBool() {
            return Boolean.parseBoolean(arg);
        }

        public char toChar() {
            return arg.isEmpty() ? ' ' : arg.charAt(0);
        }

        @Override
        public String toString() {
            return arg;
        }

        public <T extends Enum<?>> T toEnum(Class<T> enumType) {
            return toEnum(enumType, null);
        }

        public <T extends Enum<?>> T toEnum(Class<T> enumType, T fallback) {
            String arg = this.arg.replace('-', '_');
            for (T constant : enumType.getEnumConstants()) {
                if (arg.equalsIgnoreCase(constant.name())) {
                    return constant;
                }
            }

            if (fallback == null) {
                throw new IllegalArgumentException("'%s' is not a value of %s".formatted(arg, enumType.getSimpleName()));
            }
            return fallback;
        }
    }
}
