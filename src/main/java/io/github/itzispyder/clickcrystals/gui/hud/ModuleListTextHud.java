package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ModuleListHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

@Environment(EnvType.CLIENT)
public class ModuleListTextHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Module hudModule = Module.get(ModuleListHud.class);
        if (!hudModule.isEnabled()) return;

        int i = 10;
        for (Module module : system.modules().values()) {
            if (!module.isEnabled()) continue;
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
