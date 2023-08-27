package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.cc.RelativeHudElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
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

        AbstractElement settings = AbstractElement.create()
                .pos(mc.getWindow().getScaledWidth() - 30, 11)
                .dimensions(20, 20)
                .onRender((context, mouseX, mouseY, button) -> {
                    context.drawTexture(GuiTextures.SETTINGS, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
                })
                .onPress(button -> {
                    mc.setScreenAndRender(new ModuleSettingsScreen(Module.get(InGameHuds.class), false));
                })
                .build();

        this.addChild(settings);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        RenderUtils.drawHorizontalLine(context, 0, context.getScaledWindowHeight() / 2, context.getScaledWindowWidth(), 1, 0xFF8C8C8C);
        RenderUtils.drawHorizontalLine(context, 0, context.getScaledWindowHeight() - 10, context.getScaledWindowWidth(), 1, 0xFF8C8C8C);
        RenderUtils.drawHorizontalLine(context, 0, 10, context.getScaledWindowWidth(), 1, 0xFF8C8C8C);
        RenderUtils.drawVerticalLine(context, context.getScaledWindowWidth() / 2, 0, context.getScaledWindowHeight(), 1, 0xFF8C8C8C);
        RenderUtils.drawVerticalLine(context, context.getScaledWindowWidth() - 10, 0, context.getScaledWindowHeight(), 1, 0xFF8C8C8C);
        RenderUtils.drawVerticalLine(context, 10, 0, context.getScaledWindowHeight(), 1, 0xFF8C8C8C);

        if (selected != null) {
            RenderUtils.drawHorizontalLine(context, 0, selected.y, context.getScaledWindowWidth(), 1, 0xFFFFFFFF);
            RenderUtils.drawVerticalLine(context, selected.x, 0, context.getScaledWindowHeight(), 1, 0xFFFFFFFF);
            RenderUtils.drawHorizontalLine(context, 0, selected.y + selected.height - 1, context.getScaledWindowWidth(), 1, 0xFFFFFFFF);
            RenderUtils.drawVerticalLine(context, selected.x + selected.width - 1, 0, context.getScaledWindowHeight(), 1, 0xFFFFFFFF);
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
