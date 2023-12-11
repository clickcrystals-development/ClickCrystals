import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ResourceRelativeHud extends Hud {

    private final Item[] itemsWithBorder = {Items.TOTEM_OF_UNDYING, Items.END_CRYSTAL, Items.OBSIDIAN, Items.EXPERIENCE_BOTTLE};

    public ResourceRelativeHud() {
        super("resource-hud", 200, 30, 20, 20);
    }

    @Override
    public void render(DrawContext context) {
        if (!canRender()) {
            return;
        }

        int y = getY();
        int g = 2;
        int next = 16 + g;
        int margin = getX() + g;
        int caret = y + g;

        for (Item item : itemsWithBorder) {
            if (InvUtils.count(item) > 0) {
                renderBackdrop(context, margin, caret, next);
                renderItem(context, item, margin, caret);
                caret += next;
            }
        }

        drawArrowItem(context, margin, caret);
        caret += next + g;
        setHeight(caret - y);
    }

    private void renderBackdrop(DrawContext context, int x, int y, int height) {
        if (shouldRenderBorder()) {
            // Adjust these values according to your requirements
            int borderSize = 2;
            int borderColor = getArgb();
            RenderUtils.drawRect(context, x - borderSize, y - borderSize, getWidth() + borderSize * 2, height + borderSize * 2, borderColor);
        }
    }

    private void renderItem(DrawContext context, Item item, int x, int y) {
        if (InvUtils.count(item) > 0) {
            RenderUtils.drawItem(context, item.getDefaultStack(), x, y, 1.0F, InvUtils.count(item) + "");
        }
    }

    private void drawArrowItem(DrawContext context, int x, int y) {
        Item arrowItem = Items.ARROW;
        if (InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) > 0) {
            RenderUtils.drawItem(context, arrowItem.getDefaultStack(), x, y, 1.0F, InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) + "");
        }
    }

    @Override
    public boolean canRender() {
        return super.canRender() && (
                InvUtils.count(Items.TOTEM_OF_UNDYING) > 0 ||
                        InvUtils.count(Items.END_CRYSTAL) > 0 ||
                        InvUtils.count(Items.OBSIDIAN) > 0 ||
                        InvUtils.count(Items.EXPERIENCE_BOTTLE) > 0 ||
                        InvUtils.count(stack -> stack.getTranslationKey().contains("arrow")) > 0
        );
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }

    @Override
    public boolean canRenderBorder() {
        return Module.getFrom(InGameHuds.class, m -> m.renderHudBorders.getVal());
    }
}
