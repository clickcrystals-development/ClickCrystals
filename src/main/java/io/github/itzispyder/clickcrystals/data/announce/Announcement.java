package io.github.itzispyder.clickcrystals.data.announce;

import io.github.itzispyder.clickcrystals.util.StringUtils;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/cc-bulletin">https://itzispyder.github.io/cc-bulletin</a>
 */
public class Announcement {

    private final String title;
    private final String desc;
    private final Field[] fields;

    public Announcement(String title, String desc, Field... fields) {
        this.title = StringUtils.color(title);
        this.desc = StringUtils.color(desc);;
        this.fields = fields;
    }

    public String getTitle() {
        return StringUtils.color(title);
    }

    public String getDesc() {
        return StringUtils.color(desc);
    }

    public Field[] getFields() {
        return fields;
    }

    public static class Field {
        private final String title;
        private final String desc;

        public Field(String title, String desc) {
            this.title = StringUtils.color(title);
            this.desc = StringUtils.color(desc);;
        }

        public String getTitle() {
            return StringUtils.color(title);
        }

        public String getDesc() {
            return StringUtils.color(desc);
        }
    }
}
