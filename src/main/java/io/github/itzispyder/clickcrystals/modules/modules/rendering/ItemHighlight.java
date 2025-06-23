package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class ItemHighlight extends ListenerModule {

    public ItemHighlight() {
        super("item-highlight", Categories.RENDER, "Renders a highlight around dropped items to make them more visible");
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        MatrixStack matrices = e.getMatrices();
        float tickDelta = e.getTickCounter().getTickProgress(true);

        for (ItemEntity itemEntity : getItemEntities()) {
            ItemStack item = itemEntity.getStack();
            Vec3d pos = e.getOffsetPos(MathUtils.lerpEntityPosVec(itemEntity, tickDelta));
            int color = getRarityColor(item.getRarity());
            int fadeColor = 0x00FFFFFF & color;
            RenderUtils3d.fillCylGradient(matrices, pos.x, pos.y, pos.z, 0.2, 0.25, color, fadeColor);
        }
    }

    public List<ItemEntity> getItemEntities() {
        List<ItemEntity> list = new ArrayList<>();
        for (Entity ent : PlayerUtils.player().clientWorld.getEntities())
            if (ent instanceof ItemEntity item)
                list.add(item);
        return list;
    }

    private int getRarityColor(Rarity rarity) {
        switch (rarity) {
            case COMMON -> {
                return 0xFFFFFFFF;
            }
            case UNCOMMON -> {
                return 0xFFFFFF55;
            }
            case RARE -> {
                return 0xFF55FFFF;
            }
            case EPIC -> {
                return 0xFFFF55FF;
            }
            case null, default -> {
                return 0xFFAAAAAA;
            }
        }
    }
}
