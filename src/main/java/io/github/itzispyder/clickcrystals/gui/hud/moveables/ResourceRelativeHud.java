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

        if (Module.get(InGameHuds.class).hudResourceMode.getVal() == InGameHuds.ResourceHudMode.RECTANGLE) {
            renderRectangleMode(context);
        } else {
            renderSquareMode(context);
        }
    }

    private void renderRectangleMode(DrawContext context) {
        int y = getY();
        int g = 2;
        int next = 16 + g;
        int margin = getX() + g;
        int caret = y + g;

        drawItem(context, Items.TOTEM_OF_UNDYING, margin, caret);
        caret += next;
        drawItem(context, Items.END_CRYSTAL, margin, caret);
        caret += next;
        drawItem(context, Items.OBSIDIAN, margin, caret);
        caret += next;
        drawItem(context, Items.EXPERIENCE_BOTTLE, margin, caret);
        caret += next;
        RenderUtils.drawItem(context, Items.ARROW.getDefaultStack(), margin, caret, 1.0F,
                InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) + "");
        caret += next + g;
        setHeight(caret - y);
    }
//    there a problem with moving the hud when it's on renderSquareMode.
    private void renderSquareMode(DrawContext context) {
        int y = getY();
        int g = 2;
        int size = 16;
        int startX = getX() + g;
        int startY = y + g;
        int caretX = startX;
        int caretY = startY;

        drawItem(context, Items.TOTEM_OF_UNDYING, caretX, caretY);
        caretX += size + g;
        drawItem(context, Items.END_CRYSTAL, caretX, caretY);
        caretX += size + g;
        drawItem(context, Items.OBSIDIAN, caretX, caretY);
        caretX = startX;
        caretY += size + g;
        drawItem(context, Items.EXPERIENCE_BOTTLE, caretX, caretY);
        caretX += size + g;
        RenderUtils.drawItem(context, Items.ARROW.getDefaultStack(), caretX, caretY, 1.0F,
                InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) + "");

        setHeight((caretY + size + g) - y);
    }

    private void drawItem(DrawContext context, Item item, int x, int y) {
        RenderUtils.drawItem(context, item.getDefaultStack(), x, y, 1.0F, InvUtils.count(item) + "");
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudResource.getVal());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }
}
