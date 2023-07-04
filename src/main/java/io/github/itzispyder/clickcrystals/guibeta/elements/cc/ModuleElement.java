package io.github.itzispyder.clickcrystals.guibeta.elements.cc;

import io.github.itzispyder.clickcrystals.guibeta.GuiElement;
import io.github.itzispyder.clickcrystals.guibeta.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ModuleElement extends GuiElement {

    private final Module module;

    public ModuleElement(Module module, int x, int y, int width) {
        super(x, y, width, width / 4);
        this.module = module;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        Identifier texture = TexturesIdentifiers.MODULE_EMPTY_TEXTURE;

        if (module != null) {
            if (module.isEnabled()) {
                texture = TexturesIdentifiers.MODULE_ON_TEXTURE;
            }
            else {
                texture = TexturesIdentifiers.MODULE_OFF_TEXTURE;
            }
        }

        context.drawTexture(texture, x, y, 0, 0, width, height, width, height);

        if (module != null) {
            DrawableUtils.drawText(context, module.getNameLimited(), x + 7, y + (int)(height * 0.33), 0.5F, true);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        module.toggle();
    }

    public Module getModule() {
        return module;
    }
}
