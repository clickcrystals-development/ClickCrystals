package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ResourceRelativeHud extends Hud {

    public ResourceRelativeHud() {
        super("resource-hud", 200, 30, 20, 20);
    }

    @Override
    public void render(DrawContext context) {
        if (!canRender()) {
            return;
        }

        int y = getY();
        int g = 2;
        int next = 16 + g;
        int margin = getX() + g;
        int caret = y + g;

        drawItemBackdrop(context, Items.TOTEM_OF_UNDYING, margin, caret);
        caret += next;
        drawItemBackdrop(context, Items.END_CRYSTAL, margin, caret);
        caret += next;
        drawItemBackdrop(context, Items.OBSIDIAN, margin, caret);
        caret += next;
        drawItemBackdrop(context, Items.EXPERIENCE_BOTTLE, margin, caret);
        caret += next;
        drawArrowItemBackdrop(context, margin, caret);  // Updated to use ARROW instead of Items.ARROW
        caret += next + g;

        // Draw the colored square once after rendering all items
        renderBackdrop(context);

        setHeight(caret - y);
    }

    private void drawItemBackdrop(DrawContext context, Item item, int x, int y) {
        // Check if the player has the item before rendering
        if (InvUtils.count(item) > 0) {
            renderColoredSquare(context, x - 2, y - 2, getWidth() + 4, getHeight() + 4);
            RenderUtils.drawItem(context, item.getDefaultStack(), x, y, 1.0F, InvUtils.count(item) + "");
        }
    }

    private void drawArrowItemBackdrop(DrawContext context, int x, int y) {
        Item arrowItem = Items.ARROW;
        // Check if the player has arrows before rendering
        if (InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) > 0) {
            renderColoredSquare(context, x - 2, y - 2, getWidth() + 4, getHeight() + 4);
            RenderUtils.drawItem(context, arrowItem.getDefaultStack(), x, y, 1.0F, InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) + "");
        }
    }

    private void renderColoredSquare(DrawContext context, int x, int y, int width, int height) {
        int argb = Module.getFrom(InGameHuds.class, m -> m.getArgb());
        context.fillGradient(x, y, x + width, y + height, argb, argb);
    }

    @Override
    public boolean canRender() {
        return super.canRender() && (
            InvUtils.count(Items.TOTEM_OF_UNDYING) > 0 ||
            InvUtils.count(Items.END_CRYSTAL) > 0 ||
            InvUtils.count(Items.OBSIDIAN) > 0 ||
            InvUtils.count(Items.EXPERIENCE_BOTTLE) > 0 ||
            InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) > 0
        );
    }

    @Override
    public boolean canRenderBorder() {
        return Module.getFrom(InGameHuds.class, m -> m.renderHudBorders.getVal());
    }
}
