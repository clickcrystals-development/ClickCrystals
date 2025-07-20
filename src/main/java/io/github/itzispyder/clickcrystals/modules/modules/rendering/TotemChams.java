package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.ChamRagDoll;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TotemChams extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> showSelf = scGeneral.add(createBoolSetting()
            .name("show-self")
            .description("Render own totem chams")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> maxVelocity = scGeneral.add(createDoubleSetting()
            .name("max-velocity")
            .description("Max velocity of the flying parts")
            .max(1.0)
            .min(0.0)
            .def(0.1)
            .build()
    );
    public final ModuleSetting<Double> gravity = scGeneral.add(createDoubleSetting()
            .name("gravity")
            .description("Gravity of the visuals")
            .max(0.05)
            .min(0.0)
            .def(0.01)
            .decimalPlaces(2)
            .build()
    );
    public final ModuleSetting<Double> maxAge = scGeneral.add(createDoubleSetting()
            .name("max-age")
            .description("How long in seconds the visuals show for")
            .max(5.0)
            .min(3.0)
            .def(5.0)
            .build()
    );
    private final SettingSection scColor = createSettingSection("color");
    public final ModuleSetting<Integer> red = scColor.add(createIntSetting()
            .name("red")
            .description("Color value RED")
            .max(255)
            .min(0)
            .def(255)
            .build()
    );
    public final ModuleSetting<Integer> green = scColor.add(createIntSetting()
            .name("green")
            .description("Color value GREEN")
            .max(255)
            .min(0)
            .def(125)
            .build()
    );
    public final ModuleSetting<Integer> blue = scColor.add(createIntSetting()
            .name("blue")
            .description("Color value BLUE")
            .max(255)
            .min(0)
            .def(125)
            .build()
    );

    private final ConcurrentLinkedQueue<ChamRagDoll> dolls = new ConcurrentLinkedQueue<>();

    public TotemChams() {
        super("totem-chams", Categories.RENDER, "Renders a nice visual whenever a player's totem pops");
    }

    @EventHandler
    private void onEntityStatus(PacketReceiveEvent e) {
        if (PlayerUtils.invalid())
            return;
        if (!(e.getPacket() instanceof EntityStatusS2CPacket packet))
            return;
        if (packet.getStatus() != EntityStatuses.USE_TOTEM_OF_UNDYING)
            return;

        Entity entity = packet.getEntity(PlayerUtils.getWorld());
        if (entity instanceof PlayerEntity player) {
            if (player == mc.player && !showSelf.getVal())
                return;

            int maxAge = (int)(this.maxAge.getVal() * 20);
            ChamRagDoll doll = new ChamRagDoll(player, maxAge);
            dolls.add(doll);
        }
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid())
            return;

        for (ChamRagDoll doll: dolls) {
            if (doll.isAlive())
                doll.tick(maxVelocity.getVal().floatValue(), gravity.getVal().floatValue());
            else
                dolls.remove(doll);
        }
    }

    @EventHandler
    private void onRenderWorld(RenderWorldEvent e) {
        if (PlayerUtils.invalid())
            return;

        MatrixStack matrices = e.getMatrices();
        float tickDelta = e.getTickCounter().getTickProgress(true);
        for (ChamRagDoll doll : dolls)
            doll.render(matrices, getColor(), tickDelta);
    }

    public int getColor() {
        return 0x40 << 24 | red.getVal() << 16 | green.getVal() << 8 | blue.getVal();
    }
}
