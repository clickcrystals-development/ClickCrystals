package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ModulesList;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
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
        List<Module> modules = new ArrayList<>(system.modules().values().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> (mc.textRenderer.getWidth(((Module) module).getId()))).reversed())
                .toList());

        int i = 5;
        for (Module module : modules) {
            final Text display = Text.literal("  §b" + module.getName() + " ");
            final int x = mc.getWindow().getScaledWidth() - 1;
            final int y = i += 10;
            final int fillColor = 0x70000000;
            final int lineColor = 0xFFACE8FB;
            final int length = tr.getWidth(display);

            context.fill(x, y, x - length, y + 10, fillColor);
            context.fill(x, y, x + 1, y + 10, lineColor);
            RenderUtils.drawRightText(context, display, x, y, true);
        }
    }
}