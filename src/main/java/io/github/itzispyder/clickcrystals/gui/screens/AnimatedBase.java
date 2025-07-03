package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;

public abstract class AnimatedBase extends GuiScreen {

    private boolean open, playOpenAnimation, playCloseAnimation;
    private final PollingAnimator animator = new PollingAnimator(300, () -> open, Animations.UPWARDS_BOUNCE);

    public AnimatedBase(String title) {
        super(title);
        playOpenAnimation = false;
        playCloseAnimation = PlayerUtils.valid();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Matrix3x2fStack matrices = context.getMatrices();
        boolean canAnimate = open ? canPlayOpenAnimation() : canPlayCloseAnimation();
        float scale = (float) animator.getAnimation();

        if (canAnimate) {
            float x = RenderUtils.width() / 2.0F;
            float y = RenderUtils.height() / 2.0F;
//            double scaleX = x / scale - x;
//            double scaleY = y / scale - y;

            matrices.pushMatrix();
            matrices.scaleAround(scale, x, y);
        }

        super.render(context, mouseX, mouseY, delta);

        if (canAnimate)
            matrices.popMatrix();
    }

    @Override
    protected void init() {
        super.init();
        open = true;
    }

    @Override
    public void close() {
        if (!canPlayCloseAnimation()) {
            super.close();
            return;
        }
        if (!open)
            return;

        open = false;
        system.scheduler.runDelayedTask(() -> mc.execute(super::close), animator.getLength());
    }

    public boolean canPlayCloseAnimation() {
        return playCloseAnimation;
    }

    public void setPlayCloseAnimation(boolean playCloseAnimation) {
        this.playCloseAnimation = playCloseAnimation;
    }

    public boolean canPlayOpenAnimation() {
        return playOpenAnimation;
    }

    public void setPlayOpenAnimation(boolean playOpenAnimation) {
        this.playOpenAnimation = playOpenAnimation;
    }
}
