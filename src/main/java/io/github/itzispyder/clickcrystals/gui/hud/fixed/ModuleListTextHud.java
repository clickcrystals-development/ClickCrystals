package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ModulesList;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleListTextHud extends Hud {

    public ModuleListTextHud() {
        super("module-list-text-hud");
        this.setFixed(true);
    }

    @Override
    public void render(DrawContext context) {
        Module hudModule = Module.get(ModulesList.class);

        if (!hudModule.isEnabled()) return;

        TextRenderer tr = mc.textRenderer;
        List<Module> modules = new ArrayList<>(system.collectModules().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> (mc.textRenderer.getWidth(((Module) module).getId()))).reversed())
                .toList());

        int i = 0;
        for (Module module : modules) {
            Text display = Text.literal("  §b" + module.getName() + " ");
            int x = mc.getWindow().getScaledWidth() - 1;
            int y = i;
            int fillColor = 0x70000000;
            int lineColor = 0xFF008EC2;
            int length = tr.getWidth(display);

            RenderUtils.fill(context, x - length, y, length, 10, fillColor);
            RenderUtils.fill(context, x - 1, y, 2, 10, lineColor);
            RenderUtils.drawRightText(context, display, x, y + 1, true);
            i += 10;
        }
    }
}