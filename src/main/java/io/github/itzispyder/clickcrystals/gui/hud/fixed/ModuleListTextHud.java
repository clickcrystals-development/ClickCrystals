package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ArrayListHud;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.List;

public class ModuleListTextHud extends Hud {

    public ModuleListTextHud() {
        super("module-list-text-hud");
        this.setFixed(true);
    }

    @Override
    public void render(DrawContext context) {
        Module hudModule = Module.get(ArrayListHud.class);

        if (!hudModule.isEnabled()) return;

        TextRenderer tr = mc.textRenderer;
        List<Module> modules = new java.util.ArrayList<>(system.collectModules().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> (mc.textRenderer.getWidth(((Module) module).getId()))))
                .toList());

        int i = mc.getWindow().getScaledHeight() - modules.size() * 10;
        for (Module module : modules) {
            String moduleText = String.format("%s", module.getName());

            Text display = Text.literal(moduleText);
            int x = mc.getWindow().getScaledWidth() - 1;
            int y = i;
            int length = tr.getWidth(display);
            int paddingX = 2;
            int paddingY = 1;


            RenderUtils.fillRoundRectGradient(context, x - length - paddingX, y - paddingY, length + 2 * paddingX, 10 + 2 * paddingY, 5, 0xFF1CE6ED, 0xFF127073,0xFF3C6A6B,0xFF98E9EB,0xFF2B9EA1 );
//            (context, "Active Modules:", x, i - 6, true);
            RenderUtils.drawRightText(context, display, x, y + 1, true);
            i += 10;
        }
    }
}
