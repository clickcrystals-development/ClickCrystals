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
import net.minecraft.client.gui.DrawContext;
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
        super("ClickCrystals Home Screen");

        GridOrganizer grid = new GridOrganizer(baseX + 27, baseY + baseHeight - 65, 35, 35, 10, 31);
        suggestions.add(new SuggestionElement(Tex.Socials.DISCORD, "Discord", 0, 0, 35, button -> system.openUrl("https://discord.gg/tMaShNzNtP")));
        suggestions.add(new SuggestionElement(Tex.Socials.CURSEFORGE, "CurseForge", 0, 0, 35, button -> system.openUrl("https://www.curseforge.com/minecraft/mc-mods/clickcrystals")));
        suggestions.add(new SuggestionElement(Tex.Socials.YOUTUBE, "Youtube", 0, 0, 35, button -> system.openUrl("https://youtube.com/@itzispyder")));
        suggestions.add(new SuggestionElement(Tex.Socials.PLANETMC, "Planet MC", 0, 0, 35, button -> system.openUrl("https://planetminecraft.com/mod/clickcrystal")));
        suggestions.add(new SuggestionElement(Tex.ICON, "Official Site", 0, 0, 35, button -> system.openUrl("https://clickcrystals.xyz")));
        suggestions.add(new SuggestionElement(Tex.Icons.MODULES, "Browse Modules", 0, 0, 35, button -> UserInputListener.openModulesScreen()));

        suggestions.forEach(grid::addEntry);
        grid.organize();
        grid.createPanel(this, baseHeight / 4);
        grid.addAllToPanel();
        this.addChild(grid.getPanel());

        for (SuggestionElement suggestion : suggestions) {
            suggestion.y -= 50;
        }

        system.scheduler.runRepeatingTask(() -> {
            for (SuggestionElement suggestion : suggestions) {
                suggestion.y++;
            }
        }, 0, 1, 50);

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
        if (!ClickCrystals.info.getMotd().trim().isEmpty()) {
            renderMotd(context);
        }

        int caret = baseY + 70;
        int titleTrans = (int)(-50 * animator.getProgressClampedReversed());
        RenderUtils.drawCenteredText(context, "§lClickCrystals §rv" + version, baseX + baseWidth / 2, caret - titleTrans, 2.0F, true);
        caret += 20;
        RenderUtils.drawCenteredText(context, "Crystal PvP Enhanced", baseX + baseWidth / 2, caret - titleTrans, 1.0F, true);
        caret += 30;
        searchBar.x = baseX + baseWidth / 2 - searchBar.width / 2;
        searchBar.y = caret - titleTrans;
    }

    public void renderMotd(DrawContext context) {
        List<String> lines = TextUtils.wordWrap(ClickCrystals.info.getMotd(), 300 - 5 - 5, 0.9F);
        int i = 3;
        int x = baseX + baseWidth / 2 - 150;
        int y = baseY + 20;
        RenderUtils.fillRoundRect(context, x, y, 300, lines.size() * 9 + 6, 3, Shades.DARK_GRAY);
        for (String line : lines) {
            RenderUtils.drawText(context, "§e" + line, x + 5, y + i, 0.9F, false);
            i += 10;
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new HomeScreen());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == GLFW.GLFW_KEY_ENTER && this.selected == searchBar && !searchBar.getQuery().isEmpty()) {
            mc.setScreen(new SearchScreen() {{
                this.searchbar.setQuery(HomeScreen.this.searchBar.getQuery());
                this.filterByQuery(this.searchbar);
            }});
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!OPENED_BEFORE) {
            mc.setScreen(new DiscordInviteScreen());
            OPENED_BEFORE = true;
            return true;
        }

        super.mouseClicked(mouseX, mouseY, button);
        return true;
    }
}
