package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ArrayListHud;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleListTextHud extends Hud {

    private long lastRenderTime;
    private float hue;

    public ModuleListTextHud() {
        super("module-list-text-hud");
        this.setFixed(true);
        this.lastRenderTime = System.currentTimeMillis();
        this.hue = 0;
    }

    @Override
    public void render(DrawContext context) {
        Module hudModule = Module.get(ArrayListHud.class);

        if (!hudModule.isEnabled()) return;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRenderTime;
        lastRenderTime = currentTime;
        hue += (float) ((elapsedTime / 1000.0) * 30); // Change color every 30 seconds

        TextRenderer tr = mc.textRenderer;
        List<Module> modules = new ArrayList<>(system.collectModules().stream()
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

            int randomColor = Color.HSBtoRGB(hue / 360f, 0.7f, 1.0f);

            // Reduce alpha by 40%
            randomColor = (randomColor & 0x00FFFFFF) | 0xAA000000;

            RenderUtils.fillRoundRectGradient(context, x - length - paddingX, y - paddingY, length + 2 * paddingX, 10 + 2 * paddingY, 4, randomColor, 0xA6127073, 0xA63C6A6B, 0xA698E9EB, 0xA62B9EA1);
//            RenderUtils.drawText(context, "Active Modules:", x, i -1, true); -> An idea that I had 🤷‍♂️
            RenderUtils.drawRightText(context, display, x, y + 1, true);
            i += 10;
        }
    }
}

