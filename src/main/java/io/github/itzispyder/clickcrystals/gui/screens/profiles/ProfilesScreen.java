package io.github.itzispyder.clickcrystals.gui.screens.profiles;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class ProfilesScreen extends DefaultBase {

    public ProfilesScreen() {
        super("Configuration Profiles Screen");

        GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);
        grid.addEntry(new ProfileDownload(0, 0));
        grid.addEntry(new ProfileCreateNew(0, 0));
        grid.addEntry(new ProfileSelect(null, "Main Config", 0, 0));

        for (String profile : system.profiles.getCustomProfiles()) {
            ProfileSelect ps = new ProfileSelect(profile, 0, 0);
            grid.addEntry(ps);
        }

        grid.organize();
        grid.createPanel(this, contentHeight - 21);
        grid.addAllToPanel();
        this.addChild(grid.getPanel());
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, Tex.Icons.MODULES, contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, "Configuration Profiles", contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new ProfilesScreen());
    }

    private static class ProfileSelect extends ModuleElement {
        private final String profileId, displayName;
        private final AbstractElement deleteButton = AbstractElement.create()
                .pos(0, 0)
                .dimensions(50, 12)
                .onRender((context, mouseX, mouseY, button) -> {
                    RenderUtils.fillRoundHoriLine(context, button.x, button.y, button.width, button.height, Shades.GENERIC_LOW);
                    if (!ProfileSelect.this.isHovered(mouseX, mouseY)) {
                        RenderUtils.fillRoundHoriLine(context, button.x + 1, button.y + 1, button.width - 2, button.height - 2, Shades.DARK_GRAY);
                    }
                    RenderUtils.drawCenteredText(context, "Delete", button.x + button.width / 2, button.y + button.height / 3, 0.7F, false);
                })
                .build();

        public ProfileSelect(String profileId, String displayName, int x, int y) {
            super(null, x, y);
            this.profileId = profileId;
            this.displayName = displayName;
            this.setTooltip("Click to select profile");

            if (profileId != null) {
                this.addChild(deleteButton);
            }
        }

        public ProfileSelect(String profileId, int x, int y) {
            this(profileId, StringUtils.capitalizeWords(profileId), x, y);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
            }

            String text = "§7 -    " + (system.profiles.profileConfig.getCurrentProfileName().equals(displayName) ? "§7> §b" : "§7") + displayName;
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);

            deleteButton.y = y + height / 2 - deleteButton.height / 2;
            deleteButton.x = x + width - deleteButton.width - 10;
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (!deleteButton.isHovered((int)mouseX, (int)mouseY)) {
                system.profiles.switchProfile(profileId);
            }
            else {
                while (system.profiles.hasProfile(profileId)) {
                    system.profiles.deleteProfile(profileId);
                }
                mc.setScreen(new ProfilesScreen());
            }
        }
    }

    private static class ProfileCreateNew extends ModuleElement {
        private final SearchBarElement textField = new SearchBarElement(0, 0) {
            {
                this.setDefaultText("§7Enter profile name");
            }

            @Override
            public void onKey(int key, int scancode) {
                super.onKey(key, scancode);
                if (!(mc.currentScreen instanceof GuiScreen screen)) {
                    return;
                }

                if (key != GLFW.GLFW_KEY_ENTER) {
                    return;
                }
                if (getQuery().isEmpty()) {
                    screen.selected = null;
                    return;
                }

                String name = getQuery().trim()
                        .toLowerCase()
                        .replace(' ', '-')
                        .replaceAll("[^a-z_-]", "");

                system.profiles.switchProfile(name);
                mc.setScreen(new ProfilesScreen());
            }
        };

        public ProfileCreateNew(int x, int y) {
            super(null, x, y);
            this.setTooltip("§7Type the file name then hit §eENTER§7!");
            this.addChild(textField);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
            }

            String text = "Create a new config profile.";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);

            textField.y = y + height / 2 - textField.height / 2;
            textField.x = (int)(x + 30 + (mc.textRenderer.getWidth(text) * 0.7));
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (mc.currentScreen instanceof GuiScreen screen) {
                textField.setDefaultText("§c*Enter profile name*");
                screen.selected = textField;
            }
        }
    }

    private static class ProfileDownload extends ModuleElement {

        public ProfileDownload(int x, int y) {
            super(null, x, y);
            this.setTooltip("§7Click to browse profiles that you can download.");
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x6000B7FF);
            }

            String text = "Need pre-made profiles? §bDownload here ->";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            mc.setScreen(new DownloadProfileScreen());
        }
    }
}
