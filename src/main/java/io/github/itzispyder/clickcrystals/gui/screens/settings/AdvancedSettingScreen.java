package io.github.itzispyder.clickcrystals.gui.screens.settings;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class AdvancedSettingScreen extends DefaultBase {

    public AdvancedSettingScreen() {
        super("Advanced Settings Screen");

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        int caret = contentY + 25;
        int margin = contentX + 5;

        // setting groups
        // gui group
        SettingSection guiSection = new SettingSection("gui-interface");
        guiSection.add(guiSection.createBoolSetting()
                .name("Overview mode")
                .description("Change to overview mode when viewing modules. ONLY WORKS IN-GAME")
                .def(ClickCrystals.config.isOverviewMode())
                .onSettingChange(setting -> {
                    ClickCrystals.config.setOverviewMode(setting.getVal());
                    ClickCrystals.config.save();

                    system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                        if (setting.getVal()) {
                            mc.setScreen(new OverviewScreen());
                        }
                    }), 200);
                })
                .build()
        );
        SettingSectionElement guiElement = new SettingSectionElement(guiSection, margin, caret);

        // add groups to screen
        panel.addChild(guiElement);
        this.addChild(panel);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawText(context, "Advanced Settings", contentX + 10, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new AdvancedSettingScreen());
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.save();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.save();
        return true;
    }
}
