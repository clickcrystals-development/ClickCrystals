package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchResultsElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SuggestionElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.util.minecraft.TextUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AnimatedBase {

    public static boolean OPENED_BEFORE = false;
    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    public final SearchBarElement searchBar = new SearchBarElement(0, 0, 300);
    public final SearchResultsElement searchResults = new SearchResultsElement(searchBar);
    protected final Animator animator = new Animator(200, Animations.FADE_IN_AND_OUT);
    public final List<SuggestionElement> suggestions = new ArrayList<>();

    public HomeScreen() {
        super("ClickCrystals Modified Home Screen");

        // Four meaningful local actions; the button row is centered as a group.
        int iconSize = 35;
        int iconGap = 22;
        int iconCount = 4;
        int rowWidth = iconCount * iconSize + (iconCount - 1) * iconGap;
        int rowX = baseX + (baseWidth - rowWidth) / 2;
        GridOrganizer grid = new GridOrganizer(rowX, baseY + baseHeight - 65, iconSize, iconSize, iconCount, iconGap);
        suggestions.add(new SuggestionElement(Tex.Icons.MODULES, "Modules", 0, 0, iconSize,
                button -> UserInputListener.openModulesScreen()));
        suggestions.add(new SuggestionElement(Tex.ICON_CLICKSCRIPT, "Scripts", 0, 0, iconSize,
                button -> UserInputListener.scheduleOpenScreen(new ScriptsBrowsingScreen())));
        suggestions.add(new SuggestionElement(Tex.Icons.SETTINGS, "HUD Editor", 0, 0, iconSize,
                button -> UserInputListener.scheduleOpenScreen(new HudEditScreen())));
        suggestions.add(new SuggestionElement(Tex.Icons.FOLDER, "Source Code", 0, 0, iconSize,
                button -> system.openUrl("https://github.com/Gergherobrine/ClickCrystals-Modified")));

        suggestions.forEach(grid::addEntry);
        grid.organize();
        grid.createPanel(this, baseHeight / 4);
        grid.addAllToPanel();
        this.addChild(grid.getPanel());

        // added last so it renders last and registers first
        this.addChild(searchResults);
        this.addChild(searchBar);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);

        // title card
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(baseX, baseY);

        RenderUtils.fillRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Shades.TRANS_BLACK);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, -10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundTabBottom(context, 15, baseHeight / 2 - 10, baseWidth - 30, baseHeight / 2, 10, Shades.TRANS_BLACK);
        RenderUtils.drawRoundTexture(context, Tex.Backdrops.BACKDROP_HOME, 10, 10, baseWidth - 20, baseHeight / 2 + 40, 5);
        RenderUtils.fillRoundShadow(context,10, 10, baseWidth - 20, baseHeight / 2 + 40, 5, 5, 0xFF000000, 0x00000000);

        context.getMatrices().popMatrix();

        // content


        int caret = baseY + 70;
        int titleTrans = (int)(-50 * animator.getProgressClampedReversed());
        RenderUtils.drawCenteredText(context, "§lClickCrystals Modified §rv" + version, baseX + baseWidth / 2, caret - titleTrans, 2.0F, true);
        caret += 20;
        RenderUtils.drawCenteredText(context, "Gergherobrine  /  PvP Toolkit", baseX + baseWidth / 2, caret - titleTrans, 1.0F, true);
        caret += 30;
        searchBar.x = baseX + baseWidth / 2 - searchBar.width / 2;
        searchBar.y = caret - titleTrans;
    }

    @Override
    public void resize(int width, int height) {
        client.setScreen(new HomeScreen());
    }

    @Override
    public boolean keyPressed(KeyInput e) {
        super.keyPressed(e);
        if (e.getKeycode() == GLFW.GLFW_KEY_ENTER && this.selected == searchBar && !searchBar.getQuery().isEmpty()) {
            mc.setScreen(new SearchScreen() {{
                this.searchbar.setQuery(HomeScreen.this.searchBar.getQuery());
                this.filterByQuery(this.searchbar);
            }});
        }
        return true;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
super.mouseClicked(click, doubled);
        return true;
    }
}
