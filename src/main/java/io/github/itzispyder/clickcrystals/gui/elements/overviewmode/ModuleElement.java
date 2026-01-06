package io.github.itzispyder.clickcrystals.gui.elements.overviewmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthSupport;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ModuleElement extends GuiElement {

    private final Module module;
    private final boolean blacklisted;

    public ModuleElement(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setTooltip(module.getDescription());
        this.module = module;
        this.blacklisted = ModrinthSupport.isBlacklisted(module);

        if (blacklisted) {
            setTooltip(ModrinthSupport.warning);
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int color = Shades.TRANS_DARK_GRAY;

        if (module.isEnabled()) {
            color = Shades.TRANS_GRAY;
        }
        if (!blacklisted && isHovered(mouseX, mouseY) && hasParent() && mc.currentScreen instanceof GuiScreen screen && screen.hovered == getParent()) {
            color = Shades.TRANS_LIGHT_GRAY;
        }

        RenderUtils.fillRoundRect(context, x, y, width, height, 3, color);

        String text = (module.isEnabled() ? "§b" : "§7") + module.getNameLimited();
        RenderUtils.drawText(context, text, x + 5, y + height / 3, 0.8F, false);

        if (blacklisted) {
            RenderUtils.fillRect(context, x, y, width, height, 0x60000000);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (blacklisted || !isHovered((int)mouseX, (int)mouseY))
            return;

        if (button == 0) {
            module.setEnabled(!module.isEnabled(), false);
        }
        else if (button == 1 && mc.currentScreen instanceof OverviewScreen screen) {
            screen.setCurrentEditing(module, (int)mouseX, (int)mouseY);
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public Module getModule() {
        return module;
    }
}
