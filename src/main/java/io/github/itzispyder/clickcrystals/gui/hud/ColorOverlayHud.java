package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemOverlay;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InvUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.item.Items;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ColorOverlayHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        TotemOverlay totemOverlay = Module.get(TotemOverlay.class);
        Window win = mc.getWindow();

        if (totemOverlay.isEnabled() && !HotbarUtils.isTotemed() && InvUtils.has(Items.TOTEM_OF_UNDYING)) {
            if (totemOverlay.fill.getVal()) {
                this.renderColor(context, 0x30FF0000);
            }
            if (totemOverlay.border.getVal()) {
                RenderUtils.drawBorder(context,0,0,win.getScaledWidth(),win.getScaledHeight(),5,0xFFFF0000);
            }
            if (totemOverlay.icon.getVal()) {
                context.drawTexture(GuiTextures.NO_TOTEMS_ICON, (mc.getWindow().getScaledWidth()/2)+10, (mc.getWindow().getScaledHeight()/2)-10, 0,0, 20,20,20,20);
            }
            if (totemOverlay.meme.getVal()) {
                context.drawTexture(GuiTextures.NO_TOTEMS_MEME, (mc.getWindow().getScaledWidth()/2)-57, 0, 0,0,115,75,115,75);
            }
        }
    }

    private void renderColor(DrawContext context, int color) {
        final Window win = mc.getWindow();
        context.fill(0, 0, win.getWidth(), win.getHeight(), color);
    }
}