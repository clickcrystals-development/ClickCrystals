package io.github.itzispyder.clickcrystals.gui.elements.browsingmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.screens.ModuleEditScreen;
import io.github.itzispyder.clickcrystals.gui.screens.scripts.ClickScriptIDE;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ModuleElement extends GuiElement {

    private final Module module;
    private Animator animator;

    public ModuleElement(Module module, int x, int y) {
        super(x, y, 300, 15);
        super.setTooltip("§eLEFT-CLICK§7 to toggle, §eRIGHT-CLICK§7 to edit");
        this.module = module;
        this.animator = new Animator(400, Animations.FADE_IN_AND_OUT);

        if (module instanceof ScriptedModule) {
            setTooltip(getTooltip().concat(", §6MIDDLE-CLICK§7 to open IDE"));
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        boolean isAnimating = animator != null && !animator.isFinished();
        if (isAnimating) {
            context.getMatrices().pushMatrix();
            context.getMatrices().translate(-(float)(width * 0.5 * animator.getAnimationReversed()), 0);
        }
        else {
            animator = null;
        }

        if (isHovered(mouseX, mouseY)) {
            RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
        }

        String text;

        text = "  %s".formatted(module.getOnOrOff());
        RenderUtils.drawText(context, text, x, y + height / 3, 0.7F, false);
        text = " §8|   §f%s".formatted(module.getNameLimited());
        RenderUtils.drawText(context, text, x + 20, y + height / 3, 0.7F, false);
        text = "§7- %s".formatted(module.getDescriptionLimited());
        RenderUtils.drawText(context, text, x + width / 3, y + height / 3, 0.7F, false);

        if (isAnimating) {
            context.getMatrices().popMatrix();
        }
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

    public Animator getAnimator() {
        return animator;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }
}
