package io.github.itzispyder.clickcrystals.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Categories {

    public static final Category
            CRYSTALLING = new Category("Crystalling"),
            ANCHORING = new Category("Anchoring"),
            MISC = new Category("Misc"),
            OTHER = new Category("Other"),
            OPTIMIZATION = new Category("Optimization"),
            RENDERING = new Category("Rendering");

    private static final Map<String,Category> categories = new HashMap<>();

    static {
        categories.put("Crystalling",CRYSTALLING);
        categories.put("Anchoring",ANCHORING);
        categories.put("Misc",MISC);
        categories.put("Other",OTHER);
        categories.put("Optimization",OPTIMIZATION);
        categories.put("Rendering",RENDERING);
    }

    public static Map<String, Category> getCategories() {
        return new HashMap<>(categories);
    }

    public static void forEach(Consumer<Category> consumer) {
        for (Category category : getCategories().values()) consumer.accept(category);
    }
}
