package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class AnimatedBase extends GuiScreen {

    private boolean open, playOpenAnimation, playCloseAnimation;
    private final PollingAnimator animator = new PollingAnimator(300, () -> open, Animations.UPWARDS_BOUNCE);

    public AnimatedBase(String title) {
        super(title);
        playOpenAnimation = false;
        playCloseAnimation = PlayerUtils.valid();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        boolean canAnimate = open ? canPlayOpenAnimation() : canPlayCloseAnimation();
        float scale = (float) animator.getAnimation();

        if (canAnimate) {
            float x = RenderUtils.width() / 2.0F;
            float y = RenderUtils.height() / 2.0F;
//            double scaleX = x / scale - x;
//            double scaleY = y / scale - y;

            context.pose().pushMatrix();
            context.pose().scaleAround(scale, x, y);
        }

        super.extractRenderState(context, mouseX, mouseY, delta);

        if (canAnimate)
            context.pose().popMatrix();
    }

    @Override
    protected void init() {
        super.init();
        open = true;
    }

    @Override
    public void onClose() {
        if (!canPlayCloseAnimation()) {
            super.onClose();
            return;
        }
        if (!open)
            return;

        open = false;
        system.scheduler.runDelayedTask(() -> mc.execute(super::onClose), animator.getLength());
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
