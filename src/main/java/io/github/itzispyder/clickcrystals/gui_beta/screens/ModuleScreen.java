package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.ModuleElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class ModuleScreen extends DefaultBase {

    public static Category currentCategory = Categories.MISC;
    private final GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);

    public ModuleScreen() {
        super("Module Screen");

        grid.createPanel(this, contentHeight - 21);
        this.addChild(grid.getPanel());
        this.filterByCategory(currentCategory);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, currentCategory.texture(), contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, currentCategory.name(), contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    public void filterByCategory(Category category) {
        grid.clearPanel();
        grid.clear();
        List<Module> list = system.collectModules().stream()
                .filter(m -> m.getCategory() == category)
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        if (category == Categories.SCRIPTED) {
            grid.addEntry(new ScriptDetails(0, 0));
            grid.addEntry(new ScriptCreateNew(0, 0));
        }
        for (Module module : list) {
            ModuleElement e = new ModuleElement(module, 0, 0);
            grid.addEntry(e);
        }
        grid.organize();
        grid.addAllToPanel();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new ModuleScreen());
    }


    private static class ScriptDetails extends ModuleElement {
        public ScriptDetails(int x, int y) {
            super(null, x, y);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            String text = "§7To manually reload scripts, execute chat command §f%sccs reload-scripts".formatted(ClickCrystals.commandPrefix.getKeyName());
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {

        }
    }

    private static class ScriptCreateNew extends ModuleElement {
        private final SearchBarElement textField = new SearchBarElement(0, 0) {
            {
                this.setDefaultText("§7Enter module name");
            }

            @Override
            public void onKey(int key, int scancode) {
                super.onKey(key, scancode);
                if (!(mc.currentScreen instanceof GuiScreen screen)) {
                    return;
                }

                if (key == GLFW.GLFW_KEY_ENTER) {
                    if (getQuery().isEmpty()) {
                        screen.selected = null;
                        return;
                    }
                    String name = getQuery().trim()
                            .replace(' ', '-')
                            .replaceAll("[^a-zA-Z0-9_-]", "")
                            .toLowerCase();
                    File file = new File(ScriptedModule.PATH + "/" + name + ".ccs");
                    mc.setScreen(new ClickScriptIDE(file));
                }
            }
        };

        public ScriptCreateNew(int x, int y) {
            super(null, x, y);
            this.setTooltip("Click to create a custom module!");
            this.addChild(textField);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fill(context, x, y, width, height, 0x60FFFFFF);
            }

            String text = "§7Create new with ClickScript IDE - §eBETA";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);

            textField.y = y + height / 2 - textField.height / 2;
            textField.x = (int)(x + 30 + (mc.textRenderer.getWidth(text) * 0.7));
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (mc.currentScreen instanceof GuiScreen screen) {
                textField.setDefaultText("§c*Enter module name*");
                screen.selected = textField;
            }
        }
    }
}
