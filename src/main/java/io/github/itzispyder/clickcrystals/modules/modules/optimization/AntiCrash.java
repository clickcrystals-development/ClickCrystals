package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

public class AntiCrash extends Module implements Listener {

    public static final int OUT_OF_BOUNDS = 30000000;
    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scParticles = createSettingSection("particle-crashes");
    private final SettingSection scExplosions = createSettingSection("explosion-crashes");
    public final ModuleSetting<Boolean> sendFeedback = scGeneral.add(BooleanSetting.create()
            .name("send-feedback")
            .description("Notifies you if ClickCrystals has saved you from a crash!")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> maxParticleAmount = scParticles.add(DoubleSetting.create()
            .max(10000)
            .min(500)
            .decimalPlaces(1)
            .name("max-particle-amount")
            .description("Limits your client to only receive particle packets within this amount.")
            .def(500.0)
            .build()
    );
    public final ModuleSetting<Double> maxExplosionsRadius = scExplosions.add(DoubleSetting.create()
            .max(200)
            .min(100)
            .decimalPlaces(1)
            .name("max-explosion-radius")
            .description("Limits your client to only receive explosion packets within this radius.")
            .def(100.0)
            .build()
    );
    public final ModuleSetting<Double> maxExplosionsAffectedBlocks = scExplosions.add(DoubleSetting.create()
            .max(100000)
            .min(50000)
            .decimalPlaces(1)
            .name("max-explosion-blocks")
            .description("Only receive explosion packets with affected blocks being less than this value.")
            .def(100000.0)
            .build()
    );
    public final ModuleSetting<Double> maxExplosionsPlayerVelocity = scExplosions.add(DoubleSetting.create()
            .max(100000)
            .min(50000)
            .decimalPlaces(1)
            .name("max-explosion-velocity")
            .description("Only receive explosion packets with player velocities being this value.")
            .def(100000.0)
            .build()
    );

    public AntiCrash() {
        super("anti-crash", Categories.LAG,"Prevents various ways servers can crash your client. Be sure to report new crashes to us so we can add more!");
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
    private void onReceiveParticle(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ParticleS2CPacket packet) {
            int count = packet.getCount() - 1;
            double maxCount = maxParticleAmount.getVal();

            if (count > maxCount) {
                e.cancel();
                flag("§bLAG DETECTED: Particle spawned with count §7" + count + "§b, max §7" + maxCount + "§b!");
            }
        }
        else if (e.getPacket() instanceof ExplosionS2CPacket packet) {
            double x = packet.center().getX();
            double y = packet.center().getY();
            double z = packet.center().getZ();
            double pX = 0;
            double pY = 0;
            double pZ = 0;
            if (packet.playerKnockback().isPresent()) {
                pX = packet.playerKnockback().get().x;
                pY = packet.playerKnockback().get().y;
                pZ = packet.playerKnockback().get().z;
            }

            int oob = OUT_OF_BOUNDS;

            double speed = Math.abs(pX) + Math.abs(pY) + Math.abs(pZ);
            double maxSpeed = maxExplosionsPlayerVelocity.getVal();

            if (speed > maxSpeed) {
                e.cancel();
                flag("§bLAG DETECTED: Explosion spawned with player-speed §7" + speed + "§b, max §7" + maxSpeed + "§b!");
            }
            if (Math.abs(x) >= oob || Math.abs(y) >= oob || Math.abs(z) >= oob) {
                e.cancel();
                flag("§bLAG DETECTED: Explosion spawned with bounds §7(" + x + ", " + y + ", " + z + ")§b, out of bounds of §7" + oob + "§b!");
            }
        }
    }

    private void flag(String msg) {
        if (sendFeedback.getVal()) {
            ChatUtils.sendPrefixMessage(msg);
        }
    }
}
