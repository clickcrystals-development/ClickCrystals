package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.crystalling.ClickCrystal;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class ModuleEditScreen extends DefaultBase {

    private static Module previousOpened = Module.get(ClickCrystal.class);
    private final Module module;

    public ModuleEditScreen(Module module) {
        super("Editing Module " + module.getName());
        this.module = module;
        previousOpened = module;

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        int caret = contentY + 25;

        for (SettingSection section : module.getData().getSettingSections()) {
            if (section.getSettings().isEmpty()) {
                continue;
            }
            SettingSectionElement sse = new SettingSectionElement(section, contentX + 5, caret);
            panel.addChild(sse);
            caret += sse.height + 5;
        }
        this.addChild(panel);
    }

    public ModuleEditScreen() {
        this(previousOpened);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        String text;
        int caret = contentY + 10;

        RenderUtils.drawTexture(context, module.getCategory().texture(), contentX + 10, caret - 7, 15, 15);
        text = "%s%s \\ §f%s   §7§o(hover details)".formatted(isCategoryHovered(mouseX, mouseY) ? "§f" : "§7", module.getCategory().name(), module.getName());
        RenderUtils.drawText(context, text, contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);

        // description
        if (isModuleTitleHovered(mouseX, mouseY)) {
            this.renderDescription(context, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (isCategoryHovered(mouseX, mouseY)) {
            BrowsingScreen.currentCategory = module.getCategory();
            mc.setScreen(new BrowsingScreen());
        }

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        ClickCrystals.config.saveModule(module);
        ClickCrystals.config.save();
        return true;
    }

    private void renderDescription(DrawContext context, int mouseX, int mouseY) {
        List<String> lines = TextUtils.wordWrap(module.getDescription(), 250 - 2 - 2, 0.7F);
        int height = lines.size() * 8;
        int caret = mouseY - height + 1;
        int margin = mouseX + 2;

        RenderUtils.fillRect(context, mouseX, mouseY - height, 250, height, 0xD0000000);

        for (String line : lines) {
            RenderUtils.drawText(context, "§7" + line, margin, caret, 0.7F, false);
            caret += 8;
        }
    }

    private boolean isCategoryHovered(double mouseX, double mouseY) {
        int dirX = contentX;
        int dirY = contentY;
        int dirW = 80;
        int dirH = 20;
        int mX = (int)mouseX;
        int mY = (int)mouseY;
        return mX > dirX && mX < dirX + dirW && mY > dirY && mY < dirY + dirH;
    }

    private boolean isModuleTitleHovered(double mouseX, double mouseY) {
        int dirX = contentX + 80;
        int dirY = contentY;
        int dirW = contentX + contentWidth - dirX;
        int dirH = 20;
        int mX = (int)mouseX;
        int mY = (int)mouseY;
        return mX > dirX && mX < dirX + dirW && mY > dirY && mY < dirY + dirH;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new ModuleEditScreen(module));
    }
}
