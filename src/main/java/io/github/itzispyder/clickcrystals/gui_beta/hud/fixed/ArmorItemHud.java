package io.github.itzispyder.clickcrystals.gui_beta.hud.fixed;

import io.github.itzispyder.clickcrystals.gui_beta.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ArmorHud;
import net.minecraft.client.util.math.MatrixStack;

public class ArmorItemHud extends Hud {

    public ArmorItemHud() {
        super("armor-hud");
        this.setFixed(true);
    }

    @Override
    public void render(MatrixStack context) {
        ArmorHud hud = Module.get(ArmorHud.class);

        if (hud.isEnabled()) {
            hud.onRender(context);
        }
    }
}
