package io.github.itzispyder.clickcrystals.data.announce;

import io.github.itzispyder.clickcrystals.util.StringUtils;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/bulletin</a>
 */
public record Announcement(String title, String desc, Field... fields) {

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
