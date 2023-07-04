package io.github.itzispyder.clickcrystals.guibeta.elements.ui;

import io.github.itzispyder.clickcrystals.guibeta.GuiElement;
import io.github.itzispyder.clickcrystals.guibeta.TextAlignment;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.BackgroundElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.BoxElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class NoticeWidget extends GuiElement {

    private String title;
    private String message;

    public NoticeWidget(String title, String message) {
        super(0, 0, 0, 0);

        Window win = mc.getWindow();
        int w = win.getScaledWidth();
        int h = win.getScaledHeight();

        this.width = w;
        this.height = h;
        this.title = title;
        this.message = message;

        // background
        BoxElement box = new BoxElement(x, y, w, h, 0x50000000);
        this.addChild(box);

        // rear
        BackgroundElement bg = new BackgroundElement(0, 0, 200, 100);
        bg.centerIn(w, h);
        this.addChild(bg);

        // front
        WidgetElement we = new WidgetElement(0, 0, 160, 60);
        we.centerIn(w, h);
        this.addChild(we);

        // interface
        TextElement titleText = new TextElement(title, TextAlignment.CENTER, 1.0F, w / 2, we.y + 10);
        this.addChild(titleText);
        int i = 0;
        for (String line : StringUtils.wrapLines(message, 50, true)) {
            TextElement lineText = new TextElement(line, TextAlignment.CENTER, 0.7F, w / 2, titleText.y + (10 * ++i));
            this.addChild(lineText);
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.setRendering(false);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
