package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;

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
            .name("random-message-1")
            .description("The message to send.")
            .def("gg")
            .build()
    );
    public final ModuleSetting<String> message2 = scMessages.add(StringSetting.create()
            .name("random-message-2")
            .description("The message to send.")
            .def("good game")
            .build()
    );
    public final ModuleSetting<String> message3 = scMessages.add(StringSetting.create()
            .name("random-message-3")
            .description("The message to send.")
            .def("ez")
            .build()
    );
    public final ModuleSetting<String> message4 = scMessages.add(StringSetting.create()
            .name("random-message-4")
            .description("The message to send.")
            .def("noice")
            .build()
    );
    public final ModuleSetting<String> message5 = scMessages.add(StringSetting.create()
            .name("random-message-5")
            .description("The message to send.")
            .def("ha better luck next time")
            .build()
    );

    public AutoGG() {
        super("auto-gg", Categories.MISC, "Sends a message upon killing a player");
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
        if (e.getPacket() instanceof ClientboundEntityEventPacket packet && !PlayerUtils.invalid()) {
            LocalPlayer p = PlayerUtils.player();
            Entity ent = packet.getEntity(p.level());
            int status = packet.getEventId();
            boolean playerWithinRange = ent instanceof Player player && player.position().distanceTo(p.position()) < distance.getVal() && player != p;

            if (status == EntityEvent.DEATH && playerWithinRange) {
                this.sendRandomMessage();
            }
        }
    }

    public void sendRandomMessage() {
        List<String> messages = Stream.of(
                message1.getVal(),
                message2.getVal(),
                message3.getVal(),
                message4.getVal(),
                message5.getVal()
        ).filter(s -> !s.isEmpty()).toList();

        if (!messages.isEmpty()) {
            String msg = system.random.getRandomElement(messages);
            ChatUtils.sendChatMessage(msg);
        }
    }
}
