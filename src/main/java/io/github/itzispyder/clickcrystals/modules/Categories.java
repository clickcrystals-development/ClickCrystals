package io.github.itzispyder.clickcrystals.modules;

import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public abstract class Categories {

    private static final LinkedHashMap<String,Category> categories = new LinkedHashMap<>();
    public static final Category
            CRYSTALLING      = register("Crystal", "minecraft:textures/item/end_crystal.png"),
            ANCHORING        = register("Anchor",  "minecraft:textures/block/glowstone.png"),
            MINECART         = register("Cart",    "minecraft:textures/item/minecart.png"),
            MISC             = register("Misc",    "minecraft:textures/item/golden_apple.png"),
            RENDERING        = register("Render",  "minecraft:textures/block/glass.png"),
            OPTIMIZATION     = register("Lag",     "minecraft:textures/item/spyglass.png"),
            CLICKCRYSTALS    = register("Client",  "minecraft:textures/item/redstone.png");

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
