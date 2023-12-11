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
        renderBackdrop(context);

        int y = getY();
        int g = 2;
        int next = 16 + g;
        int margin = getX() + g;
        int caret = y + g;

        boolean itemsRendered = false; // Flag to check if any items are rendered

        itemsRendered |= drawItem(context, Items.TOTEM_OF_UNDYING, margin, caret);
        caret += next;
        itemsRendered |= drawItem(context, Items.END_CRYSTAL, margin, caret);
        caret += next;
        itemsRendered |= drawItem(context, Items.OBSIDIAN, margin, caret);
        caret += next;
        itemsRendered |= drawItem(context, Items.EXPERIENCE_BOTTLE, margin, caret);
        caret += next;
        itemsRendered |= drawArrowItem(context, margin, caret);
        caret += next + g;

        if (itemsRendered) {
            setHeight(caret - y);
        }
    }

    private boolean drawItem(DrawContext context, Item item, int x, int y) {
        // Check if the player has the item before rendering
        if (InvUtils.count(item) > 0) {
            RenderUtils.drawItem(context, item.getDefaultStack(), x, y, 1.0F, InvUtils.count(item) + "");
            return true; // Item rendered, set the flag to true
        }
        return false; // Item not rendered, flag remains false
    }

    private boolean drawArrowItem(DrawContext context, int x, int y) {
        Item arrowItem = Items.ARROW;
        // Check if the player has arrows before rendering
        if (InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) > 0) {
            RenderUtils.drawItem(context, arrowItem.getDefaultStack(), x, y, 1.0F, InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) + "");
            return true; // Arrow item rendered, set the flag to true
        }
        return false; // Arrow item not rendered, flag remains false
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudResource.getVal());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }

    @Override
    public boolean canRenderBorder() {
        return Module.getFrom(InGameHuds.class, m -> m.renderHudBorders.getVal());
    }
}
