package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.GuiBorders;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ScrollPanelElement extends GuiElement {

    private final GuiScreen parentScreen;
    public static final int SCROLL_MULTIPLIER = 15;
    private int remainingUp, remainingDown, limitTop, limitBottom, scrollbarY, scrollbarHeight, prevDrag;
    private boolean scrolling;

    private final Animator interpolation;
    private int interpolationLength;

    public ScrollPanelElement(GuiScreen parentScreen, int x, int y, int width, int height) {
        super(x, y, width, height);
        super.setContainer(true);
        this.parentScreen = parentScreen;
        this.interpolation = new Animator(100);

        remainingUp = remainingDown = 0;
        limitTop = y;
        limitBottom = y + height;
        scrolling = false;

        // callbacks
        parentScreen.mouseClickListeners.add((mouseX, mouseY, button, click) -> {
            if (isHovered((int)mouseX, (int)mouseY) && click.isDown()) {
                scrolling = true;
                prevDrag = (int)mouseY;
            }
            else if (scrolling && click.isRelease()) {
                scrolling = false;
            }
        });
    }

    public boolean canScrollDown() {
        return remainingDown > 0;
    }

    public boolean canScrollUp() {
        return remainingUp > 0;
    }

    public boolean canScroll() {
        return canScrollUp() || canScrollDown();
    }

    public GuiScreen getParentScreen() {
        return parentScreen;
    }

    public boolean canScrollInDirection(int amount) {
        if (amount >= 0) {
            return canScrollUp();
        }
        else {
            return canScrollDown();
        }
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        updateBounds(child);
        child.scrollOnPanel(this, 0);
    }

    public void updateBounds(GuiElement child) {
        if (child.y < limitTop) {
            limitTop = child.y;
        }
        if (child.y + child.height > limitBottom) {
            limitBottom = child.y + child.height;
        }
        remainingUp = y - limitTop;
        remainingDown = limitBottom - (y + height);
    }

    public void recalculatePositions() {
        remainingUp = remainingDown = 0;
        limitTop = y;
        limitBottom = y + height;

        for (GuiElement child : getChildren()) {
            updateBounds(child);
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        boolean bl = canRender();

        float interpolatedDelta = (float)(interpolationLength * interpolation.getProgressClampedReversed());
        context.enableScissor(x, y, x + width, y + height);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, -interpolatedDelta);

        if (bl)
            onRender(context, mouseX, mouseY);
        for (GuiElement child : this.getChildren())
            if (child.y + child.height > this.y || child.y < this.y + this.height)
                child.render(context, mouseX, mouseY);

        context.getMatrices().popMatrix();
        context.disableScissor();

        if (Module.isEnabled(GuiBorders.class))
            RenderUtils.drawRect(context, x, y, width, height, 0xFFFFFFFF);
        if (bl)
            postRender(context, mouseX, mouseY);
    }

    @Override
    public void postRender(DrawContext context, int mouseX, int mouseY) {
        if (!canScroll() || !canRender())
            return;

        double fullDoc = remainingUp + remainingDown + this.height;
        double drawStartRatio = remainingUp / fullDoc;
        double ratio = this.height / fullDoc;

        if (scrolling && mouseY != prevDrag) {
            double deltaY = mouseY - prevDrag;
            double multiplier = Math.abs(deltaY) / this.height * fullDoc;

            scrollWithMultiplier(deltaY > 0 ? -1 : 1, (int)multiplier);
            prevDrag = mouseY;
        }

        RenderUtils.fillRect(context, x + width - 6, y, 6, height, 0xFF1F1F1F);

        int drawStart = (int)(this.height * drawStartRatio);
        int drawLength = (int)(this.height * ratio);

        RenderUtils.fillRect(context, this.x + this.width - 6, this.y + drawStart, 6, drawLength, 0xFFAAAAAA);
        RenderUtils.fillRect(context, this.x + this.width - 1, this.y + drawStart, 1, drawLength, 0xFF555555);
        RenderUtils.fillRect(context, this.x + this.width - 6, this.y + drawStart + drawLength - 1, 6, 1, 0xFF555555);

        scrollbarY = this.y + drawStart;
        scrollbarHeight = drawLength;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver((int)mouseX, (int)mouseY)) {
            onClick(mouseX, mouseY, button);
        }

        for (int i = getChildren().size() - 1; i >= 0; i--) {
            if (MathUtils.oob(i, 0, getChildren().size() - 1))
                break;
            GuiElement child = getChildren().get(i);
            child.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Redefinition of mouse hover to hovering over the scroll bar, not the entire element.
     * @param mouseX mouse x
     * @param mouseY mouse y
     * @return is mouse hovered
     */
    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return super.isHovered(mouseX, mouseY) && mouseX > (x + width - 6) && mouseY > scrollbarY && mouseY < scrollbarY + scrollbarHeight;
    }

    public void onScroll(double amount) {
        interpolationLength = scrollWithMultiplier(amount, SCROLL_MULTIPLIER);
        interpolation.reset();
    }

    private int scrollWithMultiplier(double amount, int multiplier) {
        int total = 0;
        for (int i = 0; i < multiplier; i++) {
            total += scrollWithoutMultiplier(amount);
        }
        return total;
    }

    private int scrollWithoutMultiplier(double amount) {
        int a = (int)amount;
        if (!canScrollInDirection(a))
            return 0;

        for (GuiElement child : getChildren())
            child.scrollOnPanel(this, a);

        remainingDown += a;
        remainingUp -= a;
        return a;
    }
}
