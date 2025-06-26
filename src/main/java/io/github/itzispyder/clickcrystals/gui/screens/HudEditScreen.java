package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.PositionableHudElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class HudEditScreen extends GuiScreen {

    private final List<PositionableHudElement> huds;

    public HudEditScreen() {
        super("Edit Hud Screen");

        this.huds = system.huds().values().stream()
                .filter(h -> !h.isFixed())
                .map(PositionableHudElement::new)
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
                    RenderUtils.drawTexture(context,Tex.Icons.SETTINGS, button.x, button.y,button.width, button.height);
                })
                .onPress(button -> {
                    mc.setScreenAndRender(new ModuleEditScreen(Module.get(InGameHuds.class)));
                })
                .build();

        this.addChild(settings);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.fillRect(context, 0, 0, this.width, this.height, 0x90000000);

        RenderUtils.drawHorLine(context, 0, context.getScaledWindowHeight() / 2, context.getScaledWindowWidth(), 0xFF8C8C8C);
        RenderUtils.drawHorLine(context, 0, context.getScaledWindowHeight() - 10, context.getScaledWindowWidth(), 0xFF8C8C8C);
        RenderUtils.drawHorLine(context, 0, 10, context.getScaledWindowWidth(), 0xFF8C8C8C);
        RenderUtils.drawVerLine(context, context.getScaledWindowWidth() / 2, 0, context.getScaledWindowHeight(), 0xFF8C8C8C);
        RenderUtils.drawVerLine(context, context.getScaledWindowWidth() - 10, 0, context.getScaledWindowHeight(), 0xFF8C8C8C);
        RenderUtils.drawVerLine(context, 10, 0, context.getScaledWindowHeight(), 0xFF8C8C8C);

        if (selected != null) {
            RenderUtils.drawHorLine(context, 0, selected.y, context.getScaledWindowWidth(), 0xFFFFFFFF);
            RenderUtils.drawVerLine(context, selected.x, 0, context.getScaledWindowHeight(), 0xFFFFFFFF);
            RenderUtils.drawHorLine(context, 0, selected.y + selected.height - 1, context.getScaledWindowWidth(), 0xFFFFFFFF);
            RenderUtils.drawVerLine(context, selected.x + selected.width - 1, 0, context.getScaledWindowHeight(), 0xFFFFFFFF);
        }
    }

    public List<PositionableHudElement> getHuds() {
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
