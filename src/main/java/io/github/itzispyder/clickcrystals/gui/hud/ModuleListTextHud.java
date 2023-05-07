package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ModulesList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Hud for enabled modules
 */
@Environment(EnvType.CLIENT)
public class ModuleListTextHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        final Module hudModule = Module.get(ModulesList.class);

        if (!hudModule.isEnabled()) return;

        final TextRenderer tr = mc.textRenderer;
        final List<Module> modules = new ArrayList<>(system.modules().values().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> (mc.textRenderer.getWidth(((Module) module).getId()))).reversed())
                .toList());

        int i = 10;
        for (Module module : modules) {
            final Text display = Text.literal("  §b" + module.getName() + " ");
            final int x = 20;
            final int y = 40 + (i += 10);
            final int color = 0xFFFFFFFF;
            final int fillColor = 0x70000000;
            final int lineColor = 0xFFACE8FB;
            final int length = tr.getWidth(display);

            DrawableHelper.fill(matrixStack, x, y, x + length, y + 10, fillColor);
            DrawableHelper.fill(matrixStack, x, y, x + 1, y + 10, lineColor);
            DrawableHelper.drawTextWithShadow(matrixStack, tr, display, x, y, color);
        }
    }
}
