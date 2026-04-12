package io.github.itzispyder.clickcrystals.gui.misc.brushes;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class MobHeadBrush implements Global {

    public static final Map<EntityType<?>, Identifier> REGISTRY = new HashMap<>();

    public static void init(EntityType<?> type, String vanillaId) {
        String path = "textures/display/icons/entities/%s.png".formatted(vanillaId);
        Identifier identifier = Identifier.fromNamespaceAndPath(modId, path);
        REGISTRY.put(type, identifier);
    }

    public static Identifier getIdentifier(Entity entity) {
        return REGISTRY.get(entity.getType());
    }

    public static Identifier getIdentifier(EntityType<?> entity) {
        return REGISTRY.get(entity);
    }

    public static void drawHead(GuiGraphicsExtractor context, Entity entity, int x, int y, int size) {
        Identifier tex = getIdentifier(entity);
        if (tex != null)
            RenderUtils.drawTexture(context, tex, x, y, size, size);
    }

    public static void drawHead(GuiGraphicsExtractor context, EntityType<?> entity, int x, int y, int size) {
        Identifier tex = getIdentifier(entity);
        if (tex != null)
            RenderUtils.drawTexture(context, tex, x, y, size, size);
    }
}
