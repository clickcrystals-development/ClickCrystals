package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AutoDisconnect extends ListenerModule {

    private final SettingSection checks = createSettingSection("checks");
    private final SettingSection moduleSettings = createSettingSection("module-settings");
    private final SettingSection playerHealth = createSettingSection("player-health-check-settings");
    public final ModuleSetting<Double> range = checks.add(createDoubleSetting()
            .max(15)
            .min(0)
            .name("Range")
            .description("The maximum distance from the player to check for blocks/entities.")
            .def(5.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> hp = playerHealth.add(createDoubleSetting()
            .max(19)
            .min(0)
            .name("Player Health Percentage")
            .description("Automatically disconnects when health is lower or equal to this value. Set to 0 to disable.")
            .def(6.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Boolean> anchors = checks.add(createBoolSetting()
            .name("Check Anchors")
            .description("Disconnect if a respawn anchor is found within the specified range.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> glowstone = checks.add(createBoolSetting()
            .name("Check Loaded Anchors")
            .description("Disconnect if a loaded respawn anchor is found within the specified range.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> crystals = checks.add(createBoolSetting()
            .name("Check Crystals")
            .description("Disconnect if an end crystal is found within the specified range.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> newPlayers = checks.add(createBoolSetting()
            .name("Check New Players")
            .description("Disconnect if a new player is rendered within the player's render distance.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> autoDisable = moduleSettings.add(createBoolSetting()
            .name("Auto Disable")
            .description("Automatically disable this module after it triggers a disconnect.")
            .def(true)
            .build()
    );

    public AutoDisconnect() {
        super("auto-disconnect", Categories.CLIENT, "Disconnect you from the world when a certain condition is met");
    }

    private void setAutoDisable() {
        if (autoDisable.getVal()) {
            this.toggle();
        }
    }

    private void disconnectPlayer(String reason) {
        Text text = Text.literal("§3Auto-Disconnect was triggered§r\n\n§c" + reason);
        PlayerUtils.player().networkHandler.onDisconnect(new DisconnectS2CPacket(text));
        setAutoDisable();
    }

    private List<Integer> getRangeList(double maxRange) {
        return IntStream.rangeClosed(1, (int) maxRange)
                .boxed()
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid())
            return;

        ClientPlayerEntity p = PlayerUtils.player();
        World w = PlayerUtils.getWorld();
        double maxRange = range.getVal();
        List<Integer> rangeList = getRangeList(maxRange);

        if (isEnabled()) {
            if (!PlayerUtils.player().isDead() && PlayerUtils.player().getHealth() <= hp.getVal() && hp.getVal() != 0) {
                disconnectPlayer("Player health is below " + hp.getVal());
                return;
            }
        }

        if (anchors.getVal()) {
            for (Integer r : rangeList) {
                BlockPos anchorPos = PlayerUtils.getNearestBlock(r, state -> {
                    if (state.getBlock() == Blocks.RESPAWN_ANCHOR) {
                        if (glowstone.getVal()) {
                            BlockState blockState = state.getBlock().getStateWithProperties(state);
                            int charges = blockState.get(RespawnAnchorBlock.CHARGES);
                            return charges > 0;
                        } else {
                            return true;
                        }
                    }
                    return false;
                });
                if (anchorPos != null) {
                    disconnectPlayer("Respawn Anchor is in range");
                    return;
                }
            }
        }

        if (crystals.getVal()) {
            for (Integer r : rangeList) {
                Entity crystal = PlayerUtils.getNearestEntity(r, entity -> entity instanceof EndCrystalEntity);
                if (crystal != null) {
                    disconnectPlayer("End Crystal is in range");
                    return;
                }
            }
        }
        if (newPlayers.getVal()) {
            for (PlayerEntity player : w.getPlayers()) {
                if (player != p && p.squaredDistanceTo(player) <= mc.options.getViewDistance().getValue() * 16 * mc.options.getViewDistance().getValue() * 16) {
                    disconnectPlayer("New player detected within render distance");
                }
                return;
            }
        }
    }
}