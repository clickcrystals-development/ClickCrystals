package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TabListElement<T> extends GuiElement {

    protected final List<T> options;
    private Consumer<TabListElement<T>> pressAction;
    private Function<T, String> nameFunction;
    protected int selection;

    public TabListElement(List<T> options, int x, int y, int width, int height, Consumer<TabListElement<T>> pressAction, Function<T, String> nameFunction) {
        super(x, y, width, height);
        this.options = options;
        this.pressAction = pressAction;
        this.nameFunction = nameFunction;
        this.selection = 0;

        ImageElement bg = new ImageElement(GuiTextures.SMOOTH_STRIPE_HORIZONTAL, x, y, width, height);
        this.initTabList(bg);
        this.addChild(bg);
    }

    protected void initTabList(ImageElement background) {
        double sectionWidth = (double)background.width / options.size();

        for (int i = 0; i < options.size(); i++) {
            TextElement tab = new TextElement(nameFunction.apply(options.get(i)), TextAlignment.CENTER, 0.6F, x, y);
            tab.setWidth((int)sectionWidth);
            tab.setX(x + (int)(sectionWidth * i));
            tab.setY(y + (int)(height * 0.33));
            background.addChild(tab);
        }
    }

    public TabListElement(List<T> options, int x, int y, int width, int height, Consumer<TabListElement<T>> pressAction) {
        this(options, x, y, width, height, pressAction, T::toString);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        super.render(context, mouseX, mouseY);
        renderExtras(context, mouseX, mouseY);
    }

    protected void renderExtras(DrawContext context, int mouseX, int mouseY) {
        double sectionWidth = (double)width / options.size();
        int selectionX = x + (int)sectionWidth * selection;

        RenderUtils.drawHorizontalLine(context, selectionX + 5, y + height - 10, (int)sectionWidth - 10, 2, 0xD03873A9);

        for (int i = 1; i < options.size(); i++) {
            RenderUtils.drawText(context, "§7|", x + (int)(sectionWidth * i), y + (int)(height * 0.33), 0.6F, false);
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        double mousePosX = mouseX - x;
        double sectionWidth = (double)width / options.size();
        int selection = (int)Math.floor(mousePosX / sectionWidth);

        this.selection = selection;
        this.pressAction.accept(this);
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public void setSelection(T selection) {
        int i = options.indexOf(selection);
        if (i != -1) {
            setSelection(i);
        }
    }

    public List<T> getOptions() {
        return options;
    }

    public Consumer<TabListElement<T>> getPressAction() {
        return pressAction;
    }

    public void setPressAction(Consumer<TabListElement<T>> pressAction) {
        this.pressAction = pressAction;
    }

    public Function<T, String> getNameFunction() {
        return nameFunction;
    }

    public void setNameFunction(Function<T, String> nameFunction) {
        this.nameFunction = nameFunction;
    }
}
