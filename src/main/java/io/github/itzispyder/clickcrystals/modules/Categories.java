package io.github.itzispyder.clickcrystals.modules;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public abstract class Categories {

    private static final LinkedHashMap<String,Category> categories = new LinkedHashMap<>();
    public static final Category
            CRYSTALLING      = register("Crystal"),
            ANCHORING        = register("Anchor"),
            MINECART         = register("Cart"),
            MISC             = register("Misc"),
            RENDERING        = register("Render"),
            OPTIMIZATION     = register("Lag"),
            CLICKCRYSTALS    = register("Client");

    private static Category register(String name) {
        Category c = new Category(name);
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
