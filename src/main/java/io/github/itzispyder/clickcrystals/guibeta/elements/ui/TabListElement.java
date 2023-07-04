package io.github.itzispyder.clickcrystals.guibeta.elements.ui;

import io.github.itzispyder.clickcrystals.guibeta.GuiElement;
import io.github.itzispyder.clickcrystals.guibeta.TextAlignment;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TabListElement<T> extends GuiElement {

    private final List<T> options;
    private Consumer<TabListElement<T>> pressAction;
    private Function<T, String> nameFunction;
    private int selection;

    public TabListElement(List<T> options, int x, int y, int width, int height, Consumer<TabListElement<T>> pressAction, Function<T, String> nameFunction) {
        super(x, y, width, height);
        this.options = options;
        this.pressAction = pressAction;
        this.nameFunction = nameFunction;
        this.selection = 0;

        double sectionWidth = (double)width / options.size();
        WidgetElement bg = new WidgetElement(x, y, width, height);

        for (int i = 0; i < options.size(); i++) {
            TextElement tab = new TextElement(nameFunction.apply(options.get(i)), TextAlignment.CENTER, 0.6F, x, y);
            tab.setWidth((int)sectionWidth);
            tab.setX(x + (int)(sectionWidth * i));
            tab.setY(y + (int)(height * 0.33));
            bg.addChild(tab);
        }

        this.addChild(bg);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        super.render(context, mouseX, mouseY);

        double sectionWidth = (double)width / options.size();
        int selectionX = x + (int)sectionWidth * selection;

        DrawableUtils.drawHorizontalLine(context, selectionX + 5, y + height - 10, (int)sectionWidth - 10, 2, 0xD03873A9);

        for (int i = 1; i < options.size(); i++) {
            DrawableUtils.drawText(context, "§7|", x + (int)(sectionWidth * i), y + (int)(height * 0.33), 0.6F, false);
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
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
