package io.github.itzispyder.clickcrystals.modules;

import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public abstract class Categories {

    private static final LinkedHashMap<String,Category> categories = new LinkedHashMap<>();
    public static final Category
            CRYSTAL          = register("Crystal", "clickcrystals:textures/display/categories/crystal.png"),
            PVP              = register("PvP", "clickcrystals:textures/display/categories/pvp.png"),
            MISC             = register("Misc", "clickcrystals:textures/display/categories/misc.png"),
            RENDER           = register("Render", "clickcrystals:textures/display/categories/render.png"),
            LAG              = register("Lag", "clickcrystals:textures/display/categories/lag.png"),
            CLIENT           = register("Client", "clickcrystals:textures/display/categories/client.png"),
            SCRIPTED         = register("Custom Made", "clickcrystals:textures/display/categories/scripted.png");

    private static Category register(String name, String texPath) {
        Category c = new Category(name, new Identifier(texPath));
        categories.put(name, c);
        return c;
    }

    public static LinkedHashMap<String, Category> getCategories() {
        return new LinkedHashMap<>(categories);
    }

    public static void forEach(Consumer<Category> consumer) {
        for (Category category : getCategories().values()) consumer.accept(category);
    }
}
