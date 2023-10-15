package io.github.itzispyder.clickcrystals.modules;

import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public abstract class Categories {

    private static final LinkedHashMap<String,Category> categories = new LinkedHashMap<>();
    public static final Category
            CRYSTALLING      = register("Crystal", "clickcrystals:textures/display/categories/crystal.png"),
            ANCHORING        = register("Anchor",  "clickcrystals:textures/display/categories/anchor.png"),
            MINECART         = register("Cart",    "clickcrystals:textures/display/categories/cart.png"),
            MISC             = register("Misc",    "clickcrystals:textures/display/categories/misc.png"),
            RENDERING        = register("Render",  "clickcrystals:textures/display/categories/render.png"),
            OPTIMIZATION     = register("Lag",     "clickcrystals:textures/display/categories/lag.png"),
            CLICKCRYSTALS    = register("Client",  "clickcrystals:textures/display/categories/client.png");

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
