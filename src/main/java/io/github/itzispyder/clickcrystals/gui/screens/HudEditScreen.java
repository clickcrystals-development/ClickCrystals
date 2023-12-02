package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.client.RelativeHudElement;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
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
                ClickCrystals.config.saveHuds();
            }
        });

        AbstractElement settings = AbstractElement.create()
                .pos(mc.getWindow().getScaledWidth() - 30, 11)
                .dimensions(20, 20)
                .onRender((context, mouseX, mouseY, button) -> {
                    context.drawTexture(Tex.Icons.SETTINGS, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
                })
                .onPress(button -> {
                    mc.setScreenAndRender(new ModuleEditScreen(Module.get(InGameHuds.class)));
                })
                .build();

        this.addChild(settings);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
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
        client.setScreen(new HudEditScreen());
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        ClickCrystals.config.saveHuds();
        ClickCrystals.config.save();
        return true;
    }
}
