package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.entity.EquipmentSlot;

public class NoArmorRender extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> renderHelmet = scGeneral.add(createBoolSetting()
            .name("render-helmet")
            .description("Render entity's helmets.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> renderChestplate = scGeneral.add(createBoolSetting()
            .name("render-chestplate")
            .description("Render entity's chestplate.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> renderLeggings = scGeneral.add(createBoolSetting()
            .name("render-leggings")
            .description("Render entity's leggings.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> renderBoots = scGeneral.add(createBoolSetting()
            .name("render-boots")
            .description("Render entity's boots.")
            .def(false)
            .build()
    );

    public NoArmorRender() {
        super("no-armor-render", Categories.RENDER, "Make armor invisible,try to use the enemy hud with this module!");
    }

    public boolean canRender(EquipmentSlot slot) {
        boolean bl = true;

        switch (slot) {
            case HEAD -> bl = renderHelmet.getVal();
            case CHEST -> bl = renderChestplate.getVal();
            case LEGS -> bl = renderLeggings.getVal();
            case FEET -> bl = renderBoots.getVal();
        }

        return bl;
    }
}