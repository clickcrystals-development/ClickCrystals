package io.github.itzispyder.clickcrystals.gui.screens.settings;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

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
            .build()
    );
    public final ModuleSetting<Boolean> modMenu = scGui.add(scGui.createBoolSetting()
            .name("disable-mod-menu-integration")
            .description("Disable accessing the ClickCrystals GUI using Mod Menu (the button won't appear after re-launch)")
            .def(ClickCrystals.config.isDisableModMenuIntegration())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDisableModMenuIntegration(setting.getVal());
                ClickCrystals.config.save();
            })
            .build()
    );
    public final ModuleSetting<Boolean> disableCustomLoading = scGui.add(scGui.createBoolSetting()
            .name("disable-custom-loading-screen")
            .description("Disable the custom ClickCrystals resource loading screen!")
            .def(ClickCrystals.config.isDisableCustomLoading())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDisableCustomLoading(setting.getVal());
                ClickCrystals.config.save();
            })
            .build()
    );
    public final ModuleSetting<Boolean> disableModuleToggleBroadcast = scGui.add(scGui.createBoolSetting()
            .name("disable-module-toggle-broadcast")
            .description("Disable chat broadcasts when you toggle a ClickCrystals feature/module")
            .def(ClickCrystals.config.isDisableModuleToggleBroadcast())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDisableModuleToggleBroadcast(setting.getVal());
                ClickCrystals.config.save();
            })
            .build()
    );
    public final ModuleSetting<Boolean> debugMode = scGui.add(scGui.createBoolSetting()
            .name("debug-mode")
            .description("Useful while developing, for devs only ;)")
            .def(ClickCrystals.config.isDev())
            .onSettingChange(setting -> {
                ClickCrystals.config.setDevMode(setting.getVal());
                ClickCrystals.config.save();
            })
            .build()
    );

    public AdvancedSettingScreen() {
        super("Advanced Settings Screen");

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        int caret = contentY + 25;
        int margin = contentX + 5;

        // setting groups
        // gui group
        SettingSectionElement guiElement = new SettingSectionElement(scGui, margin, caret);

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
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
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
