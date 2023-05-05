package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ModulesList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
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
        Module hudModule = Module.get(ModulesList.class);
        if (!hudModule.isEnabled()) return;

        List<Module> modules = new ArrayList<>(system.modules().values().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparing(module -> ((Module) module).getId().length()).reversed())
                .toList());

        int i = 10;
        for (Module module : modules) {
            DrawableHelper.drawTextWithShadow(
                    matrixStack,
                    mc.textRenderer,
                    Text.literal("§8§l| §b§o" + module.getName()),
                    20,
                    50 + (i += 10),
                    0);
        }
    }
}
