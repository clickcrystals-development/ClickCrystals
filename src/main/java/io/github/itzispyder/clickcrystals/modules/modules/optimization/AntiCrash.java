package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

public class AntiCrash extends Module implements Listener {

    public static final int OUT_OF_BOUNDS = 30000000;
    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scParticles = getGeneralSection();
    private final SettingSection scExplosions = getGeneralSection();
    public final ModuleSetting<Boolean> sendFeedback = scGeneral.add(BooleanSetting.create()
            .name("send-feedback")
            .description("Notifies you if ClickCrystals has saved you from a crash!")
            .def(true)
            .build()
    );
    public final ModuleSetting<Integer> maxParticleAmount = scParticles.add(IntegerSetting.create()
            .max(10000)
            .min(0)
            .name("max-particle-amount")
            .description("Limits your client to only receive particle packets within this amount.")
            .def(100)
            .build()
    );
    public final ModuleSetting<Integer> maxParticleVelocity = scParticles.add(IntegerSetting.create()
            .max(100)
            .min(0)
            .name("max-particle-velocity")
            .description("Limits your client to only receive particle packets within this velocity.")
            .def(20)
            .build()
    );
    public final ModuleSetting<Integer> maxExplosionsRadius = scExplosions.add(IntegerSetting.create()
            .max(100)
            .min(10)
            .name("max-explosion-radius")
            .description("Limits your client to only receive explosion packets within this radius.")
            .def(10)
            .build()
    );
    public final ModuleSetting<Integer> maxExplosionsAffectedBlocks = scExplosions.add(IntegerSetting.create()
            .max(100000)
            .min(50000)
            .name("max-explosion-radius")
            .description("Only receive explosion packets with affected blocks being less than this value.")
            .def(100000)
            .build()
    );
    public final ModuleSetting<Integer> maxExplosionsPlayerVelocity = scExplosions.add(IntegerSetting.create()
            .max(100000)
            .min(50000)
            .name("max-explosion-velocity")
            .description("Only receive explosion packets with player velocities being this value.")
            .def(100000)
            .build()
    );

    public AntiCrash() {
        super("anti-crash", Categories.OPTIMIZATION,"Prevents various ways servers can crash your client. Be sure to report new crashes to us so we can add more!");
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
            int count = packet.getCount();
            double speed = packet.getSpeed();
            int maxCount = maxParticleAmount.getVal();
            int maxSpeed = maxParticleVelocity.getVal();

            if (speed > maxSpeed) {
                e.cancel();
                flag("§bCRASH DETECTED: Particle spawned with count §7" + count + "§b, max §7" + maxCount + "§b!");
            }
            if (count > maxCount) {
                e.cancel();
                flag("§bCRASH DETECTED: Particle spawned with speed §7" + speed + "§b, max §7" + maxSpeed + "§b!");
            }
        }
        else if (e.getPacket() instanceof ExplosionS2CPacket packet) {
            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();
            float pX = packet.getPlayerVelocityX();
            float pY = packet.getPlayerVelocityY();
            float pZ = packet.getPlayerVelocityZ();
            int oob = OUT_OF_BOUNDS;

            float radius = packet.getRadius();
            int size = packet.getAffectedBlocks().size();
            double speed = Math.abs(pX) + Math.abs(pY) + Math.abs(pZ);
            int maxSpeed = maxExplosionsPlayerVelocity.getVal();
            int maxSize = maxExplosionsAffectedBlocks.getVal();
            int maxRadius = maxExplosionsRadius.getVal();

            if (speed > maxSpeed) {
                e.cancel();
                flag("§bCRASH DETECTED: Explosion spawned with player-speed §7" + speed + "§b, max §7" + maxSpeed + "§b!");
            }
            if (size > maxSize) {
                e.cancel();
                flag("§bCRASH DETECTED: Explosion spawned with size §7" + size + "§b, max §7" + maxSize + "§b!");
            }
            if (radius > maxRadius) {
                e.cancel();
                flag("§bCRASH DETECTED: Explosion spawned with radius §7" + radius + "§b, max §7" + maxRadius + "§b!");
            }
            if (Math.abs(x) > oob || Math.abs(y) > oob || Math.abs(z) > oob) {
                e.cancel();
                flag("§bCRASH DETECTED: Explosion spawned with bounds §7(" + x + ", " + y + ", " + z + ")§b, out of bounds of §7" + oob + "§b!");
            }
        }
    }

    private void flag(String msg) {
        if (sendFeedback.getVal()) {
            ChatUtils.sendPrefixMessage(msg);
        }
    }
}
