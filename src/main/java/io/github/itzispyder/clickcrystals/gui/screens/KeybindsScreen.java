package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.cc.settings.KeybindSettingElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.DividerElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import net.minecraft.client.MinecraftClient;

import java.util.Comparator;
import java.util.List;

public class KeybindsScreen extends ClickCrystalsBase {

    public KeybindsScreen() {
        super("ClickCrystals Keybinds");
    }

    @Override
    protected void init() {
        int x = nav.x + nav.width + 10;
        int y = base.y + 10;

        ScrollPanelElement main = new ScrollPanelElement(this, x, y, base.width - nav.width - 30, base.height - 20);
        this.addChild(main);

        // title
        TextElement title = new TextElement("ClickCrystal Keybinds", TextAlignment.LEFT, 0.8F, x, y += 10);
        TextElement desc = new TextElement("§7All registered binds: [Client Binds, Module Binds]", TextAlignment.LEFT, 0.6F, title.x, y += 15);
        main.addChild(title);
        main.addChild(desc);

        // client binds
        y += 10;
        List<Keybind> clientBinds = system.keybinds().stream()
                .sorted(Comparator.comparing(Keybind::getId))
                .filter(bind -> !bind.getId().equals("module-toggle-keybind"))
                .toList();
        DividerElement divider2 = new DividerElement("Client Binds (" + clientBinds.size() + ")", title.x, y += 10, main.width, 0, 0.6F);
        main.addChild(divider2);

        for (Keybind bind : clientBinds) {
            KeybindSetting setting = new KeybindSetting(bind.getId(), "", bind);
            KeybindSettingElement element = setting.toGuiElement(title.x, y += 12);
            main.addChild(element);
        }

        // module binds
        y += 10;
        List<Module> modules = system.modules().values().stream()
                .sorted(Comparator.comparing(Module::getId))
                .toList();
        DividerElement divider = new DividerElement("Module Binds (" + modules.size() + ")", title.x, y += 10, main.width, 0, 0.6F);
        main.addChild(divider);

        for (Module module : modules) {
            Keybind bind = module.getData().getBind();
            KeybindSetting setting = new KeybindSetting(module.getId(), "", bind);
            KeybindSettingElement element = setting.toGuiElement(title.x, y += 12);
            main.addChild(element);
        }
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        ClickCrystalsBase.openClickCrystalsMenu();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.save();
        return true;
    }
}
