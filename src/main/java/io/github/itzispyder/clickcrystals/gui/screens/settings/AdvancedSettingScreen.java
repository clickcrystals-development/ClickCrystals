package io.github.itzispyder.clickcrystals.gui.screens.settings;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.ClientTheme;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;

public class AdvancedSettingScreen extends DefaultBase {

    private final SettingSection scGui = new SettingSection("gui-interface");
    public final ModuleSetting<Boolean> overviewMode = scGui.add(scGui.createBoolSetting()
            .name("overview-mode")
            .description("Change to overview mode when viewing modules. ONLY WORKS IN-GAME")
            .def(ClickCrystals.config.isOverviewMode())
            .onSettingChange(setting -> {
                ClickCrystals.config.setOverviewMode(setting.getVal());
                ClickCrystals.config.save();

                system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                    if (setting.getVal() && PlayerUtils.valid()) {
                        mc.setScreen(new OverviewScreen());
                    }
                }), 200);
            })
            .build());
    public final ModuleSetting<Boolean> modMenu = scGui.add(scGui.createBoolSetting()
            .name("disable-mod-menu-integration")
            .description(
                    "Disable accessing the ClickCrystals GUI using Mod Menu (the button won't appear after re-launch)")
            .def(ClickCrystals.config.isDisableModMenuIntegration())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDisableModMenuIntegration(setting.getVal());
                ClickCrystals.config.save();
            })
            .build());
    public final ModuleSetting<Boolean> disableCustomLoading = scGui.add(scGui.createBoolSetting()
            .name("disable-custom-loading-screen")
            .description("Disable the custom ClickCrystals resource loading screen!")
            .def(ClickCrystals.config.isDisableCustomLoading())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDisableCustomLoading(setting.getVal());
                ClickCrystals.config.save();
            })
            .build());
    public final ModuleSetting<Boolean> disableModuleToggleBroadcast = scGui.add(scGui.createBoolSetting()
            .name("disable-module-toggle-broadcast")
            .description("Disable chat broadcasts when you toggle a ClickCrystals feature/module")
            .def(ClickCrystals.config.isDisableModuleToggleBroadcast())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDisableModuleToggleBroadcast(setting.getVal());
                ClickCrystals.config.save();
            })
            .build());
    public final ModuleSetting<Boolean> debugMode = scGui.add(scGui.createBoolSetting()
            .name("debug-mode")
            .description("Useful while developing, for devs only ;)")
            .def(ClickCrystals.config.isDev())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDevMode(setting.getVal());
                ClickCrystals.config.save();
            })
            .build());

    // ------------------------------------------------------------------
    // Client Theme section
    // ------------------------------------------------------------------
    private final SettingSection scTheme = new SettingSection("client-theme");
    public final ModuleSetting<String> themeColor = scTheme.add(scTheme.createStringSetting()
            .name("theme-color")
            .description("Primary accent color for the entire ClickCrystals UI. "
                    + "Enter any hex value in #RRGGBB format (e.g. #00B7FF). "
                    + "Default #00B7FF is the original ClickCrystals blue.")
            .def(ClickCrystals.config.getClientThemeColor())
            .onSettingChange(setting -> {
                String input = setting.getVal().trim();
                // Normalise: accept 'RRGGBB' without leading #
                if (!input.startsWith("#"))
                    input = "#" + input;
                Color parsed = Color.parse(input);
                // Fall back to default if the string is invalid
                int argb = parsed.getHexOpaque();
                if (argb == Color.BLACK.getHexOpaque()
                        && !input.equalsIgnoreCase("#000000")
                        && !input.equalsIgnoreCase("#000")) {
                    argb = ClientTheme.DEFAULT_PRIMARY;
                }
                ClientTheme.setTheme(argb);
                ClickCrystals.config.setClientThemeColor(ClientTheme.getThemeHex());
                ClickCrystals.config.save();
            })
            .build());

    public AdvancedSettingScreen() {
        super("Advanced Settings Screen");

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5,
                contentHeight - 21);
        int caret = contentY + 25;
        int margin = contentX + 5;

        // setting groups
        SettingSectionElement themeElement = new SettingSectionElement(scTheme, margin, caret);
        caret += themeElement.height + 5;
        SettingSectionElement guiElement = new SettingSectionElement(scGui, margin, caret);

        // add groups to screen
        panel.addChild(themeElement);
        panel.addChild(guiElement);
        this.addChild(panel);
    }

    @Override
    public void baseRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawText(context, "Advanced Settings", contentX + 10, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    @Override
    public void resize(int width, int height) {
        minecraft.setScreen(new AdvancedSettingScreen());
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.save();
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        super.mouseReleased(click);
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.save();
        return true;
    }
}
