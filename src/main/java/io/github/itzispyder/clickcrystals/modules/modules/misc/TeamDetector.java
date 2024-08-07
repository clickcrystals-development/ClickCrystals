package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;

public class TeamDetector extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<TeamsMethod> teamFindingMethod = scGeneral.add(EnumSetting.create(TeamsMethod.class)
            .name("team-detection-mode")
            .description("TeamsMethod for team detection.")
            .def(TeamsMethod.COLOR_NAME)
            .build()
    );
    public final ModuleSetting<Boolean> autoDisable = scGeneral.add(createBoolSetting()
            .name("auto-disable")
            .description("disable the module if failed to find a similar team.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> cancelCcs = scGeneral.add(createBoolSetting()
            .name("cancel-ccs")
            .description("disable ccs snapping and attacking.")
            .def(false)
            .build()
    );

    public TeamDetector() {
        super("team-detector", Categories.MISC, "Finding teams and disable attacking your own team");
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (autoDisable.getVal())
            ChatUtils.sendPrefixMessage("Didn't find any teams, toggling off");
    }

    @EventHandler
    private void onPlayerAttackEntityEvent(PlayerAttackEntityEvent e) {
        if (e.getEntity() instanceof PlayerEntity player && EntityUtils.isTeammate(player))
            e.setCancelled(true);
        else if (autoDisable.getVal())
            this.setEnabled(false);
    }

    public enum TeamsMethod {
        SCOREBOARD,
        COLOR_NAME
    }
}