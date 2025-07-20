package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;
import java.util.function.Supplier;

public class ButtonElement extends GuiElement {

    public static final Function<Supplier<? extends Screen>, ClickAction> OPEN_SCREEN = supplier -> (mx, my, self) -> {
        mc.execute(() -> mc.setScreen(supplier.get()));
    };

    private ClickAction clickCallback;
    private String text;
    private int borderRadius;
    private int fillColor, hoverColor;
    private boolean mouseDown;
    private final Animator animator = new PollingAnimator(300, () -> {
        var c = InteractionUtils.getCursor();
        return this.isHovered(c.x, c.y);
    });

    public ButtonElement(String text, int x, int y, int w, int h, int r, int fillColor, int hoverColor, ClickAction onClick) {
        super(x, y, w, h);
        this.text = text;
        this.borderRadius = r;
        this.clickCallback = onClick;
        this.fillColor = fillColor;
        this.hoverColor = hoverColor;
    }

    public ButtonElement(String text, int x, int y, int w, int h, int r, ClickAction onClick) {
        this(text, x, y, w, h, r, Shades.GRAY, Shades.GENERIC, onClick);
    }

    public ButtonElement(String text, int x, int y, int w, int h, ClickAction onClick) {
        this(text, x, y, w, h, h / 2, onClick);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int color = Color.lerp(fillColor, hoverColor, animator.getAnimation());
        int clickColor = new Color(hoverColor).darker(2).getHex();
        RenderUtils.fillRoundRect(context, x, y, width, height, borderRadius, mouseDown ? clickColor : color);
        RenderUtils.drawCenteredText(context, text, x + width / 2, y + (height - mc.textRenderer.fontHeight) / 2, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        clickCallback.onClick((int)mouseX, (int)mouseY, this);
        mouseDown = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int button) {
        super.onRelease(mouseX, mouseY, button);
        mouseDown = false;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ClickAction getClickCallback() {
        return clickCallback;
    }

    public void setClickCallback(ClickAction clickCallback) {
        this.clickCallback = clickCallback;
    }

    @FunctionalInterface
    public interface ClickAction {
        void onClick(int mx, int my, ButtonElement self);
    }
}
