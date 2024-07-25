package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AutoDisconnect extends DummyModule implements Global, Listener {
    public AutoDisconnect() {
        super("auto-disconnect", Categories.CLIENT, "disconnect you from the world when a certain condition is met");
    }

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection playerHealth = createSettingSection("player-health-check-settings");
    public final ModuleSetting<Double> range = scGeneral.add(createDoubleSetting()
            .max(15)
            .min(0)
            .name("Range")
            .description("The maximum distance from the player to check for blocks/entities.")
            .def(5.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Boolean> checkPlayerHealth = playerHealth.add(createBoolSetting()
            .name("Check Player Health")
            .description("Disconnect if the player health has reached a certain level (check the option below).")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> selfPlayerHealth = playerHealth.add(createDoubleSetting()
            .max(100)
            .min(0)
            .name("Player Health Percentage")
            .description("Disconnect if the player's health percentage falls below this value.")
            .def(20.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Boolean> checkAnchors = scGeneral.add(createBoolSetting()
            .name("Check Anchors")
            .description("Disconnect if a respawn anchor is found within the specified range.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> checkCrystals = scGeneral.add(createBoolSetting()
            .name("Check Crystals")
            .description("Disconnect if an end crystal is found within the specified range.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> autoDisable = scGeneral.add(createBoolSetting()
            .name("Auto Disable")
            .description("Automatically disable this module after it triggers a disconnect.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> checkNewPlayers = scGeneral.add(createBoolSetting()
            .name("Check New Players")
            .description("Disconnect if a new player is rendered within the player's render distance.")
            .def(false)
            .build()
    );


    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    private void setAutoDisable() {
        if (autoDisable.getVal()) {
            this.toggle();
        }
    }

    private void disconnectPlayer(String reason) {
        MutableText text = Text.literal("§3Auto-Disconnect was triggered§r");
        text = text.append(Text.literal("\n\n" + reason).withColor(Colors.RED));
        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(text));
    }

    private List<Integer> getRangeList(double maxRange) {
        return IntStream.rangeClosed(1, (int) maxRange)
                .boxed()
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid()) return;

        double maxRange = range.getVal();
        List<Integer> rangeList = getRangeList(maxRange);

        if (Module.get(AutoDisconnect.class).isEnabled()) {
            double playerHealthPercentage = (mc.player.getHealth() / mc.player.getMaxHealth()) * 100;
            if (checkPlayerHealth.getVal() && playerHealthPercentage < selfPlayerHealth.getVal()) {
                disconnectPlayer("player health is below " + selfPlayerHealth.getVal() + "%");
                setAutoDisable();
                return;
            }
        }

        if (checkAnchors.getVal()) {
            for (Integer r : rangeList) {
                BlockPos anchorPos = PlayerUtils.getNearestBlock(r, state -> state.getBlock() == Blocks.RESPAWN_ANCHOR);
                if (anchorPos != null) {
                    disconnectPlayer("Respawn Anchor is in range");
                    setAutoDisable();
                    return;
                }
            }
        }

        if (checkCrystals.getVal()) {
            for (Integer r : rangeList) {
                Entity crystal = PlayerUtils.getNearestEntity(r, entity -> entity instanceof EndCrystalEntity);
                if (crystal != null) {
                    disconnectPlayer("End Crystal is in range");
                    setAutoDisable();
                    return;
                }
            }
        }
        if (checkNewPlayers.getVal()) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player != mc.player && mc.player.squaredDistanceTo(player) <= mc.options.getViewDistance().getValue() * mc.options.getViewDistance().getValue()) {
                    disconnectPlayer("new player detected within render distance");
                    setAutoDisable();
                }
                return;
            }
        }
    }
}