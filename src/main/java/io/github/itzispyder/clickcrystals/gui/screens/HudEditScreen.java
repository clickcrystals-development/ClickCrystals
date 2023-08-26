package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.cc.RelativeHudElement;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class HudEditScreen extends GuiScreen {

    private final List<RelativeHudElement> huds;

    public HudEditScreen() {
        super("Edit Hud Screen");

        this.huds = system.huds().values().stream()
                .filter(h -> !h.isFixed())
                .map(RelativeHudElement::new)
                .toList();

        huds.forEach(this::addChild);

        super.mouseClickListeners.add((mouseX, mouseY, button, click) -> {
            if (click.isRelease()) {
                Hud.saveConfigHuds();
            }
        });
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        if (selected != null) {
            RenderUtils.drawHorizontalLine(context, 0, selected.y, context.getScaledWindowWidth(), 1, 0xFFFFFFFF);
            RenderUtils.drawVerticalLine(context, selected.x, 0, context.getScaledWindowHeight(), 1, 0xFFFFFFFF);
        }
    }

    public List<RelativeHudElement> getHuds() {
        return huds;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        client.setScreenAndRender(new HudEditScreen());
    }
}
