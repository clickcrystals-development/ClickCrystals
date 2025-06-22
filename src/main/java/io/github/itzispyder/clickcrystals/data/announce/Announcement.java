package io.github.itzispyder.clickcrystals.data.announce;

import io.github.itzispyder.clickcrystals.util.StringUtils;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin.json">https://itzispyder.github.io/clickcrystals/bulletin.json</a>
 */
public record Announcement(int order, String title, String desc, Field... fields) {

    public Announcement(String title, String desc, Field... fields) {
        this(0, title, desc, fields);
    }

    @Override
    public String title() {
        return StringUtils.color(title);
    }

    @Override
    public String desc() {
        return StringUtils.color(desc);
    }


    public record Field(String title, String desc) {

        @Override
        public String title() {
            return StringUtils.color(title);
        }

        @Override
        public String desc() {
            return StringUtils.color(desc);
        }
    }
}
