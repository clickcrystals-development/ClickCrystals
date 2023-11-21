package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

public class SettingScreen extends DefaultBase {

    public SettingScreen() {
        super("Settings Screen");

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        int caret = contentY + 25;
        int margin = contentX + 5;

        // setting groups
        // client keybind group
        List<Keybind> clientBinds = system.keybinds().stream()
                .sorted(Comparator.comparing(Keybind::getId))
                .filter(bind -> !bind.getId().equals("module-toggle-keybind"))
                .toList();
        SettingSection clientBindsSection = new SettingSection("client-keybinds");
        for (Keybind bind : clientBinds) {
            KeybindSetting bindSetting = new KeybindSetting(bind.getId(), "Client bind.", bind);
            clientBindsSection.add(bindSetting);
        }
        SettingSectionElement clientBindsElement = new SettingSectionElement(clientBindsSection, margin, caret);
        caret += clientBindsElement.height + 5;

        // module keybind group
        List<Module> modules = system.collectModules().stream()
                .sorted(Comparator.comparing(Module::getId))
                .toList();
        SettingSection moduleBindsSection = new SettingSection("module-keybinds");
        for (Module module : modules) {
            Keybind bind = module.getData().getBind();
            KeybindSetting bindSetting = new KeybindSetting(module.getId(), "Module bind.", bind);
            moduleBindsSection.add(bindSetting);
        }
        SettingSectionElement moduleBindsElement = new SettingSectionElement(moduleBindsSection, margin, caret);


        // add groups to screen
        panel.addChild(clientBindsElement);
        panel.addChild(moduleBindsElement);
        this.addChild(panel);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawText(context, "Client Settings", contentX + 10, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new SettingScreen());
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
