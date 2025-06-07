package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class HealthAsBar extends Module {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> showValueText = scGeneral.add(BooleanSetting.create()
            .name("show-value-text")
            .description("Displays a text showing how much hearts you have.")
            .def(true)
            .build()
    );

    public HealthAsBar() {
        super("health-as-bar", Categories.RENDER, "Renders your health bar as a singular bar to prevent lag. Recommended for NBT pvp");
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void renderHealthBar(DrawContext context, int x, int y, float maxHealth, int lastHealth, int health, int absorption) {
        render(context, x, y, maxHealth, lastHealth, health, absorption, showValueText.getVal());
    }

    public static void render(DrawContext context, int x, int y, float maxHealth, int lastHealth, int health, int absorption, boolean showValue) {
        int height = 8;
        int width = height * 10;

        float ratioPrev = health / (maxHealth + absorption);
        float ratioCurr = lastHealth / (maxHealth + absorption);
        float ratioAbs = absorption / (maxHealth + absorption);

        int widthPrev = (int)(ratioPrev * width);
        int widthNormal = (int)(ratioCurr * width);
        int widthAbsorp = (int)(ratioAbs * width);

        RenderUtils.fillRect(context, x, y, width, height, 0xAA000000); // base
        RenderUtils.fillRect(context, x, y, widthPrev, height, 0x90D93F24); // last hp

        RenderUtils.fillRect(context, x, y, widthNormal, height, 0xFFD93F24); // current hp
        RenderUtils.fillRect(context, x, y, widthNormal, (int)(height * 0.33), 0xAAF18B78); // current hp glint

        RenderUtils.fillRect(context, x + widthNormal, y, widthAbsorp, height, 0xFFFEDA00); // current abs
        RenderUtils.fillRect(context, x + widthNormal, y, widthAbsorp, (int)(height * 0.33), 0xAAFEDA00); // current abs glint
        RenderUtils.drawRect(context, x, y, width, height, 0xAA000000); // border

        if (showValue) {
            double hearts = MathUtils.round((lastHealth + absorption) / 2.0, 10);
            String text = hearts + "";
            int textWidth = (int)(mc.textRenderer.getWidth(text) * 0.6);
            int textX = x + widthNormal - textWidth - 2;

            textX = Math.max(textX, x + 2);
            RenderUtils.drawText(context, text, textX, y + (int)(height * 0.25), 0.6F, true);
        }
    }
}
