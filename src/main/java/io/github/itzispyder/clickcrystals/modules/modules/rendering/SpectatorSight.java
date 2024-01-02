package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class SpectatorSight extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> entitySetting = scGeneral.add(createBoolSetting()
            .name("entity")
            .description("Render invisible entities.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> playerSetting = scGeneral.add(createBoolSetting()
            .name("player")
            .description("Render invisible players.")
            .def(true)
            .build()
    );

    public SpectatorSight() {
        super("spectator-sight", Categories.RENDER, "Renders invisible entities the way you see them in spectator mode. Never get sneak attacked again!");
    }

    public boolean canRender(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return playerSetting.getVal();
        }
        return entitySetting.getVal();
    }
}
