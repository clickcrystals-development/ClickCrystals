package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.client.EntityStatusType;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.List;

public class AutoGG extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scMessages = createSettingSection("random-messages");
    public final ModuleSetting<Double> distance = scGeneral.add(DoubleSetting.create()
            .max(10.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("entity-range")
            .description("Distance for entity range.")
            .def(7.5)
            .build()
    );
    public final ModuleSetting<String> message1 = scMessages.add(StringSetting.create()
            .name("the-message-to-send")
            .description("Random choice 1")
            .def("gg")
            .build()
    );
    public final ModuleSetting<String> message2 = scMessages.add(StringSetting.create()
            .name("")
            .description("Random choice 2")
            .def("good game")
            .build()
    );
    public final ModuleSetting<String> message3 = scMessages.add(StringSetting.create()
            .name("")
            .description("Random choice 3")
            .def("ez")
            .build()
    );
    public final ModuleSetting<String> message4 = scMessages.add(StringSetting.create()
            .name("")
            .description("Random choice 4")
            .def("noice")
            .build()
    );
    public final ModuleSetting<String> message5 = scMessages.add(StringSetting.create()
            .name("")
            .description("Random choice 5")
            .def("ha better luck next time")
            .build()
    );

    public AutoGG() {
        super("auto-gg", Categories.MISC, "Sends a message upon killing a player.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onReceivePacket(PacketReceiveEvent e) {
        if (e.getPacket() instanceof EntityStatusS2CPacket packet && !PlayerUtils.playerNull()) {
            ClientPlayerEntity p = PlayerUtils.player();
            Entity ent = packet.getEntity(p.getWorld());
            int status = packet.getStatus();
            boolean playerWithinRange = ent instanceof PlayerEntity player && player.getPos().distanceTo(p.getPos()) < distance.getVal() && player != p;

            if (status == EntityStatusType.DEATH && playerWithinRange) {
                ChatUtils.sendChatMessage(getRandomMessage());
            }
        }
    }

    public String getRandomMessage() {
        return new Randomizer<>(List.of(
                message1.getVal(),
                message2.getVal(),
                message3.getVal(),
                message4.getVal(),
                message5.getVal()
        )).pickRand();
    }
}
