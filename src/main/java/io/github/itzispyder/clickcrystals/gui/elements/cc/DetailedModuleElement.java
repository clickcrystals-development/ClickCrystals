package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class DetailedModuleElement extends GuiElement {

    private final Module module;

    public DetailedModuleElement(Module module, int x, int y, int width) {
        super(x, y, width, width / 2);
        this.module = module;

        WidgetElement bg = new WidgetElement(x, y, width, height);
        this.addChild(bg);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        String prefix = module.isEnabled() ? "§b" : "§c";
        DrawableUtils.drawText(context, prefix + module.getName(), x + 5, y + 5, 0.8F, true);
        int i = 0;
        for (String line : StringUtils.wrapLines(module.getDescription(), 30, true)) {
            DrawableUtils.drawText(context, "§7" + line, x + 5, y + 13 + (int)(i++ * 4.5), 0.4F, true);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        mc.player.playSound(SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.MASTER, 0.8F, 2);

        module.setEnabled(!module.isEnabled(), false);
    }

    public Module getModule() {
        return module;
    }
}
