package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ResourcePositionableHud extends Hud {

    public ResourcePositionableHud() {
        super("resource-hud", 200, 30, 20, 20);
    }

    @Override
    public void render(GuiGraphics context, float tickDelta) {
        renderBackdrop(context);

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
        RenderUtils.drawItem(context, Items.ARROW.getDefaultInstance(), margin, caret, 1.0F,
                InvUtils.count(stack -> stack.getItem().getDescriptionId().contains("arrow")) + "");
        caret += next + g;
        setHeight(caret - y);
    }

    private void drawItem(GuiGraphics context, Item item, int x, int y) {
        RenderUtils.drawItem(context, item.getDefaultInstance(), x, y, 1.0F, InvUtils.count(item) + "");
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
