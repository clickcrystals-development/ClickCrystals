package io.github.itzispyder.clickcrystals.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalMenuScreen;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class TestWidget extends ClickableWidget {

    public TestWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderTexture(0, ClickCrystalMenuScreen.SCREEN_TITLE_BANNER_TEXTURE);
        DrawableHelper.drawTexture(matrices, getX(),getY(),getWidth(),getHeight(),0,0,getWidth(),getHeight(),getWidth(),getHeight());
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public GuiNavigationPath getFocusedPath() {
        return super.getFocusedPath();
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }
}
