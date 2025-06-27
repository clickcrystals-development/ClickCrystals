package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.RenderInventorySlotEvent;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
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

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<RenderType> renderType = scGeneral.add(createEnumSetting(RenderType.class)
            .name("Render Type")
            .description("Where to render the highlight")
            .def(RenderType.BOTH)
            .build()
    );
    public final ModuleSetting<Rarity> rarityFilter = scGeneral.add(createEnumSetting(Rarity.class)
            .name("Rarity Filter")
            .description("Only render items of this rarity or higher")
            .def(Rarity.COMMON)
            .build()
    );

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        if (!renderType.getVal().renderWorld() || !isEnabled())
            return;

        MatrixStack matrices = e.getMatrices();
        float tickDelta = e.getTickCounter().getTickProgress(true);

        for (ItemEntity itemEntity : getItemEntities()) {
            ItemStack item = itemEntity.getStack();
            Vec3d pos = e.getOffsetPos(MathUtils.lerpEntityPosVec(itemEntity, tickDelta));
            Rarity rarity = item.getRarity();

            if (rarity.ordinal() < rarityFilter.getVal().ordinal())
                continue;

            int color = getRarityColor(rarity);
            int fadeColor = 0x00FFFFFF & color;
            RenderUtils3d.fillCylGradient(matrices, pos.x, pos.y, pos.z, 0.2, 0.25, color, fadeColor);
        }
    }

    @EventHandler
    public void onDrawSlot(RenderInventorySlotEvent e) {
        ItemStack item = e.getItem();

        if (!renderType.getVal().renderGui() || item.isEmpty())
            return;
        if (item.getRarity().ordinal() < rarityFilter.getVal().ordinal())
            return;

        RenderUtils.fillRect(
                e.getDrawContext(),
                e.getSlotX(), e.getSlotY(), 16, 16,
                getRarityColor(item.getRarity())
        );
    }

    public List<ItemEntity> getItemEntities() {
        List<ItemEntity> list = new ArrayList<>();
        for (Entity ent : PlayerUtils.player().clientWorld.getEntities())
            if (ent instanceof ItemEntity item)
                list.add(item);
        return list;
    }

    public int getRarityColor(Rarity rarity) {
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

    public enum RenderType {
        GUI,
        WORLD,
        BOTH;

        public boolean renderGui() {
            return this == GUI || this == BOTH;
        }

        public boolean renderWorld() {
            return this == WORLD || this == BOTH;
        }
    }
}
