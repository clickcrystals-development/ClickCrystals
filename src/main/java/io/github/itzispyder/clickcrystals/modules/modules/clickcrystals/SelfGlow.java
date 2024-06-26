package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class SelfGlow extends DummyModule {
    public SelfGlow() {
        super("self-glow", Categories.CLIENT, "Am I Glowing?");
    }


    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Color> glowColor = scGeneral.add(createEnumSetting(Color.class)
            .name("glow-color")
            .description("Set the color of the glowing effect.")
            .def(Color.CYAN)
            .build()
    );

    public enum Color {
        RED(255, 0, 0, 255),
        GREEN(0, 255, 0, 255),
        BLUE(0, 0, 255, 255),
        YELLOW(255, 255, 0, 255),
        WHITE(255, 255, 255, 255),
        BLACK(0, 0, 0, 255),
        ORANGE(255, 165, 0, 255),
        PURPLE(128, 0, 128, 255),
        CYAN(0, 255, 255, 255);

        private final int red;
        private final int green;
        private final int blue;
        private final int alpha;

        Color(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public int[] getRGBA() {
            return new int[]{red, green, blue, alpha};
        }
    }
}
