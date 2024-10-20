package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;

public class TeamDetector extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<TeamsMethod> teamFindingMethod = scGeneral.add(EnumSetting.create(TeamsMethod.class)
            .name("team-detection-mode")
            .description("TeamsMethod for team detection.")
            .def(TeamsMethod.COLOR_NAME)
            .build()
    );
    public final ModuleSetting<String> playerNames = scGeneral.add(createStringSetting()
            .name("manual-team-players")
            .description("Add players by their usernames. Use commas to separate names.")
            .def("")
            .build()
    );
    public final ModuleSetting<Boolean> cancelCcs = scGeneral.add(createBoolSetting()
            .name("cancel-ccs")
            .description("disable ccs snapping and attacking.")
            .def(true)
            .build()
    );

    public TeamDetector() {
        super("team-detector", Categories.MISC, "Finding teams and disable attacking your own team");
    }

    @EventHandler
    private void onPlayerAttackEntityEvent(PlayerAttackEntityEvent e) {
        if (e.getEntity() instanceof PlayerEntity player) {
            boolean isTeammate = EntityUtils.isTeammate(player);
            boolean isInManualList = Arrays.stream(playerNames.getVal().split(",")).toList().contains(player.getName().toString());
            if (isTeammate || isInManualList) {
                e.cancel();
            }
        }
    }


    public enum TeamsMethod {
        SCOREBOARD,
        COLOR_NAME
    }
}