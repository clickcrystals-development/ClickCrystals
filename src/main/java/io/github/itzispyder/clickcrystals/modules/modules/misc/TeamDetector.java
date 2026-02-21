package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
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
    public final ModuleSetting<String> playerNames = scGeneral.add(createStringSetting()
            .name("manual-team-players")
            .description("Add players by their usernames. Use commas to separate names.")
            .def("")
            .build()
    );
    public final ModuleSetting<Boolean> protectTeammates = scGeneral.add(createBoolSetting()
            .name("protect-teammates")
            .description("Prevent attacking detected teammates.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> cancelCcs = scGeneral.add(createBoolSetting()
            .name("cancel-ccs")
            .description("Disable ClickCrystals scripting attacks on teammates.")
            .def(true)
            .build()
    );

    public TeamDetector() {
        super("team-detector", Categories.MISC, "Finding teams and disable attacking your own team");
    }

    @EventHandler
    private void onPlayerAttackEntityEvent(PlayerAttackEntityEvent e) {
        if (!protectTeammates.getVal())
            return;
        if (e.getEntity() instanceof PlayerEntity p && EntityUtils.isTeammate(p))
            e.cancel();
    }

    @EventHandler
    private void onMiddleClick(MouseClickEvent e) {
        if (e.getButton() != 2 || e.getAction() != ClickType.CLICK || !e.isScreenNull())
            return;
        if (!(mc.targetedEntity instanceof PlayerEntity player))
            return;

        String playerName = player.getName().getString();
        String current = playerNames.getVal();

        if (current.toLowerCase().contains(playerName.toLowerCase())) { // remove teammate
            String updated = current.replaceAll(",?\\w+,?", "");
            playerNames.setVal(updated);
            ChatUtils.sendPrefixMessage("§c✖ Removed " + playerName + " from team");
        }
        else { // add teammate
            String updated = current.isEmpty() ? playerName : current + "," + playerName;
            playerNames.setVal(updated);
            ChatUtils.sendPrefixMessage("§a✓ Added " + playerName + " to team");
        }
        e.setCancelled(true);
    }


    public enum TeamsMethod {
        SCOREBOARD,
        COLOR_NAME
    }
}