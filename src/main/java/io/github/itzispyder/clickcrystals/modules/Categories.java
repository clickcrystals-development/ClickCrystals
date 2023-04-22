package io.github.itzispyder.clickcrystals.modules;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.itzispyder.clickcrystals.ClickCrystals.modId;

public abstract class Categories {

    public static final Category CRYSTALLING = new Category("Crystalling",new Identifier(modId,"textures/gui/category_crystalling.png"));
    public static final Category ANCHORING = new Category("Anchoring",new Identifier(modId,"textures/gui/category_anchoring.png"));
    public static final Category MISC = new Category("Misc",new Identifier(modId,"textures/gui/category_misc.png"));
    public static final Category OTHER = new Category("Other",new Identifier(modId,"textures/gui/category_other.png"));
    public static final Category OPTIMIZATION = new Category("Optimization",new Identifier(modId,"textures/gui/category_optimization.png"));


    private static final Map<String,Category> categories = new HashMap<>();

    static {
        categories.put("Crystalling",CRYSTALLING);
        categories.put("Anchoring",ANCHORING);
        categories.put("Misc",MISC);
        categories.put("Other",OTHER);
        categories.put("Optimization",OPTIMIZATION);
    }

    public static Map<String, Category> getCategories() {
        return new HashMap<>(categories);
    }

    public static void forEach(Consumer<Category> consumer) {
        for (Category category : getCategories().values()) consumer.accept(category);
    }
}
