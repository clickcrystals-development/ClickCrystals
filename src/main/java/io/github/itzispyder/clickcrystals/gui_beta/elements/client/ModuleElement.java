package io.github.itzispyder.clickcrystals.gui_beta.elements.client;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.screens.ClickScriptIDE;
import io.github.itzispyder.clickcrystals.gui_beta.screens.ModuleEditScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleElement extends GuiElement {

    private final Module module;

    public ModuleElement(Module module, int x, int y) {
        super(x, y, 300, 15);
        super.setTooltip("§eLEFT-CLICK§7 to toggle, §eRIGHT-CLICK§7 to edit");
        this.module = module;

        if (module instanceof ScriptedModule) {
            setTooltip(getTooltip().concat(", §6MIDDLE-CLICK§7 to open IDE"));
        }
    }

    @Override
    public void onRender(MatrixStack context, int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            RenderUtils.fill(context, x, y, width, height, 0x60FFFFFF);
        }

        String text;

        text = "  %s".formatted(module.getOnOrOff());
        RenderUtils.drawText(context, text, x, y + height / 3, 0.7F, false);
        text = " §8|   §f%s".formatted(module.getNameLimited());
        RenderUtils.drawText(context, text, x + 20, y + height / 3, 0.7F, false);
        text = "§7- %s".formatted(module.getDescriptionLimited());
        RenderUtils.drawText(context, text, x + width / 3, y + height / 3, 0.7F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            module.setEnabled(!module.isEnabled(), false);
        }
        else if (button == 1) {
            mc.setScreen(new ModuleEditScreen(module));
        }
        else if (button == 2 && module instanceof ScriptedModule m) {
            mc.setScreen(new ClickScriptIDE(m));
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + (width - 6) && mouseY > y && mouseY < y + height;
    }

    public Module getModule() {
        return module;
    }
}
