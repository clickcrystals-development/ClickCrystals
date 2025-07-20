package io.github.itzispyder.clickcrystals.gui.elements.common;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Function;
import java.util.function.Supplier;

public class AbstractElement extends GuiElement {

    public static final Function<Supplier<String>, RenderAction<AbstractElement>> RENDER_BUTTON = (stringSupplier) -> (context, mouseX, mouseY, button) -> {
        RenderUtils.fillRoundRect(context, button.x, button.y, button.width, button.height, 3, button.isMouseOver(mouseX, mouseY) ? 0x40FFFFFF : 0x00000000);
        RenderUtils.drawRoundRect(context, button.x, button.y, button.width, button.height, 3, 0xFFFFFFFF);

        int textY = button.y + (int)(button.height - mc.textRenderer.fontHeight * 0.9);
        RenderUtils.drawCenteredText(context, stringSupplier.get(), button.x + button.width / 2, textY, 0.9F, false);
    };


    private final RenderAction<AbstractElement> onRender;
    private final PressAction<AbstractElement> onPress;

    public AbstractElement(int x, int y, int width, int height, RenderAction<AbstractElement> onRender, PressAction<AbstractElement> pressAction, String tooltip, long tooltipDelay) {
        super(x, y, width, height);
        super.setTooltip(tooltip);
        super.setTooltipDelay(tooltipDelay);
        this.onRender = onRender;
        this.onPress = pressAction;
    }

    public AbstractElement(int x, int y, int width, int height, RenderAction<AbstractElement> onRender, PressAction<AbstractElement> pressAction) {
        this(x, y, width, height, onRender, pressAction, null, 500L);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.onRender.onRender(context, mouseX, mouseY, this);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.onPress.onPress(this);
    }

    public RenderAction<AbstractElement> getRenderAction() {
        return onRender;
    }

    public PressAction<AbstractElement> getPressAction() {
        return onPress;
    }

    public static Builder create() {
        return new Builder();
    }


    public static class Builder {
        private int x, y, width, height;
        private RenderAction<AbstractElement> onRender;
        private PressAction<AbstractElement> onPress;
        private String tooltip;
        private long tooltipDelay;

        public Builder() {
            onRender = (context, mouseX, mouseY, button) -> {};
            onPress = button -> {};
            x = y = width = height = 0;
            tooltip = null;
            tooltipDelay = 500L;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder dimensions(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder onRender(RenderAction<AbstractElement> onRender) {
            this.onRender = onRender;
            return this;
        }

        public Builder onPress(PressAction<AbstractElement> onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder tooltip(String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder tooltipDelay(long tooltipDelay) {
            this.tooltipDelay = tooltipDelay;
            return this;
        }

        public AbstractElement build() {
            return new AbstractElement(x, y, width, height, onRender, onPress, tooltip, tooltipDelay);
        }
    }
}
