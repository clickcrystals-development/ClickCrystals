package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.cc.settings.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.MinecraftClient;

public class ModuleSettingsScreen extends ClickCrystalsBase {

    private final ScrollPanelElement main;
    private final Module module;

    public ModuleSettingsScreen(Module module) {
        super(module.getName());
        this.module = module;
        this.main = new ScrollPanelElement(nav.x + nav.width + 10, base.y + 10, base.width - nav.width - 30, base.height - 20);
        this.addChild(main);
    }

    @Override
    protected void init() {
        TextElement title = new TextElement(module.getName(), TextAlignment.LEFT, 0.8F, nav.x + nav.width + 10, base.y + 10);
        int caret = title.y + 10;
        for (String line : StringUtils.wrapLines(module.getDescription(), 60, true)) {
            TextElement desc = new TextElement("§7" + line, TextAlignment.LEFT, 0.6F, title.x, caret);
            main.addChild(desc);
            caret += 6;
        }
        main.addChild(title);

        caret += 10;
        for (SettingSection group : module.getData().getSettingSections()) {
            SettingSectionElement ge = new SettingSectionElement(group, title.x, caret, base.width - nav.width - 20, 10, 0.6F);
            main.addChild(ge);
            caret += ge.getHeight() + 2;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        Module.saveModule(module);
        return true;
    }

    @Override
    public void close() {
        ClickCrystalsBase.openClickCrystalsMenu();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        ClickCrystalsBase.openClickCrystalsMenu();
    }

    public Module getModule() {
        return module;
    }
}
