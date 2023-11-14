package io.github.itzispyder.clickcrystals.client.clickscript;

import java.util.regex.PatternSyntaxException;

public record ScriptArgs(String... args) {

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
        if (args.length == 0) {
            return new Arg("");
        }
        return new Arg(args[Math.min(Math.max(index, 0), args.length - 1)]);
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
        return get(index).stringValue().equalsIgnoreCase(arg);
    }

    public void executeAll(int begin) {
        ClickScript.executeSingle(getAll(begin).stringValue());
    }

    public int getSize() {
        return args.length;
    }

    public static class Arg {
        private final String arg;

        public Arg(String arg) {
            this.arg = arg;
        }

        public int intValue() {
            try {
                return Integer.parseInt(arg.replaceAll("[^0-9-+]", ""));
            } catch (NumberFormatException | PatternSyntaxException ex) {
                return 0;
            }
        }

        public long longValue() {
            try {
                return Long.parseLong(arg.replaceAll("[^0-9-+]", ""));
            } catch (NumberFormatException | PatternSyntaxException ex) {
                return 0L;
            }
        }

        public byte byteValue() {
            try {
                return Byte.parseByte(arg.replaceAll("[^0-9-+]", ""));
            } catch (NumberFormatException | PatternSyntaxException ex) {
                return 0;
            }
        }

        public short shortValue() {
            try {
                return Short.parseShort(arg.replaceAll("[^0-9-+]", ""));
            } catch (NumberFormatException | PatternSyntaxException ex) {
                return 0;
            }
        }

        public double doubleValue() {
            try {
                return Double.parseDouble(arg.replaceAll("[^0-9-+e.]", ""));
            } catch (NumberFormatException | PatternSyntaxException ex) {
                return 0.0;
            }
        }

        public float floatValue() {
            try {
                return Float.parseFloat(arg.replaceAll("[^0-9-+e.]", ""));
            } catch (NumberFormatException | PatternSyntaxException ex) {
                return 0.0F;
            }
        }

        public boolean booleanValue() {
            return Boolean.parseBoolean(arg);
        }

        public char charValue() {
            return arg.isEmpty() ? ' ' : arg.charAt(0);
        }

        public String stringValue() {
            return arg;
        }

        public <T extends Enum<?>> T enumValue(Class<T> enumType, T fallback) {
            for (T constant : enumType.getEnumConstants()) {
                if (arg.equalsIgnoreCase(constant.name())) {
                    return constant;
                }
            }
            return fallback;
        }
    }
}
