package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.HashSet;
import java.util.Set;

public class TeamDetector extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();

    private String cachedNamesRaw;
    private Set<String> cachedNames = Set.of();

    public final ModuleSetting<TeamsMethod> teamFindingMethod = scGeneral.add(EnumSetting.create(TeamsMethod.class)
            .name("team-detection-mode")
            .description("Method used to automatically detect teams.")
            .def(TeamsMethod.COLOR_NAME)
            .build()
    );

    public final ModuleSetting<String> playerNames = scGeneral.add(createStringSetting()
            .name("manual-team-players")
            .description("Manually add players by their exact usernames. Separate names using commas.")
            .visibleWhen(() -> teamFindingMethod.getVal() == TeamsMethod.MANUAL)
            .def("")
            .build()
    );

    public final ModuleSetting<Boolean> protectTeammates = scGeneral.add(createBoolSetting()
            .name("protect-teammates")
            .description("Cancels target tracking and melee attacks on detected teammates.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> cancelCcs = scGeneral.add(createBoolSetting()
            .name("cancel-ccs")
            .description("Prevents internal ClickCrystals scripting triggers from attacking friendly targets.")
            .def(true)
            .build()
    );

    public TeamDetector() {
        super("team-detector", Categories.MISC, "Detect team alignments and prevent friendly fire against teammates");
    }

    @EventHandler
    private void onPlayerAttackEntityEvent(PlayerAttackEntityEvent e) {
        if (!protectTeammates.getVal())
            return;
        if (e.getEntity() instanceof Player p && isTeammate(p))
            e.cancel();
    }

    @EventHandler
    private void onMiddleClick(MouseClickEvent e) {
        if (e.getButton() != 2 || e.getAction() != ClickType.CLICK || !e.isScreenNull())
            return;
        if (!(mc.crosshairPickEntity instanceof Player player))
            return;

        String playerName = player.getName().getString();
        String current = playerNames.getVal();

        if (current.toLowerCase().contains(playerName.toLowerCase())) {
            String updated = current.replaceAll("(?i),?\\b" + playerName + "\\b,?", "")
                    .replaceAll("^,", "")
                    .replaceAll(",$", "");
            playerNames.setVal(updated);
            ChatUtils.sendPrefixMessage("§cRemoved " + playerName + " from team");
        }
        else {
            String updated = current.isEmpty() ? playerName : current + "," + playerName;
            playerNames.setVal(updated);
            ChatUtils.sendPrefixMessage("§aAdded " + playerName + " to team");
        }
        e.setCancelled(true);
    }

    public static boolean isTeammate(Player target) {
        if (PlayerUtils.invalid() || target == null || target == PlayerUtils.player())
            return false;

        TeamDetector td = Module.get(TeamDetector.class);
        if (!td.isEnabled())
            return false;

        if (td.teamNames().contains(target.getName().getString().toLowerCase()))
            return true;

        return switch (td.teamFindingMethod.getVal()) {
            case SCOREBOARD -> isSameScoreboardTeam(target);
            case COLOR_NAME -> isSameColorNameTeam(target);
            case LEATHER_ARMOR -> isSameLeatherArmorColor(target);
            default -> false;
        };
    }

    public static boolean shouldCancelCcsAttack(Player target) {
        return Module.get(TeamDetector.class).cancelCcs.getVal() && isTeammate(target);
    }

    private Set<String> teamNames() {
        String raw = playerNames.getVal();
        if (!raw.equals(cachedNamesRaw)) {
            cachedNamesRaw = raw;
            Set<String> names = new HashSet<>();
            for (String name : raw.split(",")) {
                String trimmed = name.trim().toLowerCase();
                if (!trimmed.isEmpty())
                    names.add(trimmed);
            }
            cachedNames = names;
        }
        return cachedNames;
    }

    private static boolean isSameScoreboardTeam(Player player) {
        Scoreboard scoreboard = PlayerUtils.getWorld().getScoreboard();
        PlayerTeam team1 = scoreboard.getPlayersTeam(PlayerUtils.player().getName().getString());
        PlayerTeam team2 = scoreboard.getPlayersTeam(player.getName().getString());
        return team1 != null && team2 != null && team1.getName().equals(team2.getName());
    }

    private static boolean isSameColorNameTeam(Player player) {
        int playerColor = PlayerUtils.player().getTeamColor();
        int targetColor = player.getTeamColor();
        return playerColor == targetColor && playerColor != ChatUtils.getFormattingChar(ChatFormatting.WHITE);
    }

    private static boolean isSameLeatherArmorColor(Player target) {
        Player client = PlayerUtils.player();
        EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

        boolean foundLeatherPiece = false;

        for (EquipmentSlot slot : slots) {
            ItemStack clientStack = client.getItemBySlot(slot);
            ItemStack targetStack = target.getItemBySlot(slot);

            boolean clientIsLeather = clientStack.getItem() instanceof DyeableArmorItem;
            boolean targetIsLeather = targetStack.getItem() instanceof DyeableArmorItem;

            if (clientIsLeather != targetIsLeather) {
                return false;
            }

            if (clientIsLeather) {
                foundLeatherPiece = true;
                DyeableArmorItem clientItem = (DyeableArmorItem) clientStack.getItem();
                DyeableArmorItem targetItem = (DyeableArmorItem) targetStack.getItem();

                if (clientItem.getColor(clientStack) != targetItem.getColor(targetStack)) {
                    return false;
                }
            }
        }

        return foundLeatherPiece;
    }

    public enum TeamsMethod {
        SCOREBOARD,
        COLOR_NAME,
        LEATHER_ARMOR,
        MANUAL
    }
}
