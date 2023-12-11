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

        renderItemBackdrop(context, Items.TOTEM_OF_UNDYING, margin, caret);
        caret += next;
        renderItemBackdrop(context, Items.END_CRYSTAL, margin, caret);
        caret += next;
        renderItemBackdrop(context, Items.OBSIDIAN, margin, caret);
        caret += next;
        renderItemBackdrop(context, Items.EXPERIENCE_BOTTLE, margin, caret);
        caret += next;
        renderItemBackdrop(context, Items.ARROW, margin, caret);  // Updated to use ARROW instead of Items.ARROW
        caret += next + g;
        setHeight(caret - y);
    }

    private void renderItemBackdrop(DrawContext context, Item item, int x, int y) {
        // Check if the player has the item before rendering
        if (InvUtils.count(item) > 0) {
            // Render the colored square only for the specific item being rendered
            renderColoredSquare(context, x - 2, y - 2, getWidth() + 4, getHeight() + 4);
            RenderUtils.drawItem(context, item.getDefaultStack(), x, y, 1.0F, InvUtils.count(item) + "");
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
