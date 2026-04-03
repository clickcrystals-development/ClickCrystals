package io.github.itzispyder.clickcrystals.modules.modules.movement;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module implements Listener {

    // ============================================
    // SETTING SECTIONS
    // ============================================

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scBypass = createSettingSection("bypass");
    private final SettingSection scTiming = createSettingSection("timing");

    // ============================================
    // GENERAL SETTINGS
    // ============================================

    private final ModuleSetting<String> mode = scGeneral.add(createStringSetting()
            .name("mode")
            .description("Speed mode: ZERO_THREE_STACK, STRAFE, BHOP, VANILLA")
            .def("ZERO_THREE_STACK")
            .build()
    );

    private final ModuleSetting<Double> speed = scGeneral.add(createDoubleSetting()
            .name("speed")
            .description("Speed multiplier")
            .def(2.0)
            .min(0.1)
            .max(10.0)
            .build()
    );

    private final ModuleSetting<Boolean> onlyOnGround = scGeneral.add(createBoolSetting()
            .name("only-on-ground")
            .description("Only boost when on ground")
            .def(false)
            .build()
    );

    private final ModuleSetting<Boolean> autoJump = scGeneral.add(createBoolSetting()
            .name("auto-jump")
            .description("Automatically jump for bhop modes")
            .def(true)
            .build()
    );

    // ============================================
    // BYPASS SETTINGS (0.03 STACK SPECIFIC)
    // ============================================

    private final ModuleSetting<Integer> stackLimit = scBypass.add(createIntSetting()
            .name("stack-limit")
            .description("Maximum consecutive no-position packets (GrimAC threshold: 20)")
            .def(18)
            .min(1)
            .max(50)
            .build()
    );

    private final ModuleSetting<Integer> resetInterval = scBypass.add(createIntSetting()
            .name("reset-interval")
            .description("Send position packet every N ticks to reset counter")
            .def(15)
            .min(1)
            .max(30)
            .build()
    );

    private final ModuleSetting<Boolean> smartReset = scBypass.add(createBoolSetting()
            .name("smart-reset")
            .description("Automatically reset before hitting anticheat threshold")
            .def(true)
            .build()
    );

    private final ModuleSetting<Boolean> mixPositionPackets = scBypass.add(createBoolSetting()
            .name("mix-position-packets")
            .description("Randomly send position packets to look more legit")
            .def(true)
            .build()
    );

    // ============================================
    // TIMING SETTINGS
    // ============================================

    private final ModuleSetting<Integer> burstTicks = scTiming.add(createIntSetting()
            .name("burst-ticks")
            .description("Number of ticks to burst speed")
            .def(10)
            .min(1)
            .max(40)
            .build()
    );

    private final ModuleSetting<Integer> cooldownTicks = scTiming.add(createIntSetting()
            .name("cooldown-ticks")
            .description("Ticks to wait between bursts")
            .def(5)
            .min(0)
            .max(40)
            .build()
    );

    private final ModuleSetting<Boolean> burstMode = scTiming.add(createBoolSetting()
            .name("burst-mode")
            .description("Use burst + cooldown pattern instead of constant speed")
            .def(false)
            .build()
    );

    // ============================================
    // INTERNAL FIELDS
    // ============================================

    private int noPositionPacketCount = 0;
    private int tickCounter = 0;
    private int burstCounter = 0;
    private boolean inBurst = true;
    private double lastX = 0;
    private double lastY = 0;
    private double lastZ = 0;
    private float lastYaw = 0;
    private float lastPitch = 0;
    private boolean wasOnGround = false;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public Speed() {
        super("speed", Categories.MISC, "Gives you a speed boost using various methods");
    }

    // ============================================
    // MODULE LIFECYCLE
    // ============================================

    @Override
    protected void onEnable() {
        system.addListener(this);
        noPositionPacketCount = 0;
        tickCounter = 0;
        burstCounter = 0;
        inBurst = true;

        if (mc.player != null) {
            lastX = mc.player.getX();
            lastY = mc.player.getY();
            lastZ = mc.player.getZ();
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
            wasOnGround = mc.player.isOnGround();
        }
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        // Send final position packet to sync with server
        if (mc.player != null) {
            sendPositionPacket(true);
        }
    }

    // ============================================
    // EVENT HANDLERS
    // ============================================

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (mc.player == null || mc.world == null) return;

        tickCounter++;

        // Burst mode logic
        if (burstMode.getVal()) {
            if (inBurst) {
                burstCounter++;
                if (burstCounter >= burstTicks.getVal()) {
                    inBurst = false;
                    burstCounter = 0;
                }
            } else {
                burstCounter++;
                if (burstCounter >= cooldownTicks.getVal()) {
                    inBurst = true;
                    burstCounter = 0;
                    // Reset packet counter when starting new burst
                    noPositionPacketCount = 0;
                    sendPositionPacket(true);
                }
                return; // Don't apply speed during cooldown
            }
        }

        // Apply speed based on mode
        String speedMode = mode.getVal().toUpperCase();

        switch (speedMode) {
            case "ZERO_THREE_STACK" -> applyZeroThreeStack();
            case "STRAFE" -> applyStrafe();
            case "BHOP" -> applyBhop();
            case "VANILLA" -> applyVanilla();
        }

        // Smart reset before hitting threshold
        if (smartReset.getVal() && noPositionPacketCount >= stackLimit.getVal() - 2) {
            sendPositionPacket(true);
            noPositionPacketCount = 0;
        }

        // Reset interval
        if (tickCounter % resetInterval.getVal() == 0) {
            sendPositionPacket(true);
            noPositionPacketCount = 0;
        }

        // Random position packet mixing
        if (mixPositionPackets.getVal() && Math.random() < 0.15) {
            sendPositionPacket(true);
            noPositionPacketCount = 0;
        }
    }

    @EventHandler
    private void onPacketSend(PacketSendEvent e) {
        if (!(e.getPacket() instanceof PlayerMoveC2SPacket packet)) return;

        // Track our own position packets
        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround ||
            packet instanceof PlayerMoveC2SPacket.Full) {
            // Position packet sent
            noPositionPacketCount = 0;
        }
    }

    // ============================================
    // SPEED MODES
    // ============================================

    /**
     * 0.03 TICK STACKING EXPLOIT
     * 
     * This exploits GrimAC's vulnerability where consecutive no-position
     * packets accumulate 0.03 movement possibilities without limits.
     * 
     * How it works:
     * 1. Send PlayerMoveC2SPacket.OnGroundOnly (no position data)
     * 2. Server calls addZeroPointThreeToPossibilities()
     * 3. Movement predictions allow +0.03 blocks
     * 4. Repeat N times to stack speed
     * 5. Reset before hitting detection threshold (20 packets)
     */
    private void applyZeroThreeStack() {
        if (mc.player == null) return;

        // Check if we should apply speed
        if (onlyOnGround.getVal() && !mc.player.isOnGround()) return;

        // Check if we can send more no-position packets
        if (noPositionPacketCount >= stackLimit.getVal()) {
            // Force position sync
            sendPositionPacket(true);
            noPositionPacketCount = 0;
            return;
        }

        // Get movement direction
        Vec3d velocity = mc.player.getVelocity();
        double motionX = velocity.x;
        double motionZ = velocity.z;

        // Only boost horizontal movement
        if (Math.abs(motionX) > 0.001 || Math.abs(motionZ) > 0.001) {
            // Apply speed multiplier to current velocity
            double multiplier = speed.getVal();
            mc.player.setVelocity(motionX * multiplier, velocity.y, motionZ * multiplier);

            // Send no-position packet to trigger 0.03 stacking
            sendNoPositionPacket();
            noPositionPacketCount++;
        }
    }

    /**
     * STRAFE MODE
     * Traditional strafe speed with smart packet management
     */
    private void applyStrafe() {
        if (mc.player == null) return;
        if (onlyOnGround.getVal() && !mc.player.isOnGround()) return;

        double forward = 0;
        double strafe = 0;

        if (mc.options.forwardKey.isPressed()) forward += 1;
        if (mc.options.backKey.isPressed()) forward -= 1;
        if (mc.options.leftKey.isPressed()) strafe += 1;
        if (mc.options.rightKey.isPressed()) strafe -= 1;

        if (forward == 0 && strafe == 0) return;

        double yaw = Math.toRadians(mc.player.getYaw());
        double multiplier = speed.getVal() * 0.2;

        double motionX = (forward * Math.sin(yaw) + strafe * Math.cos(yaw)) * multiplier;
        double motionZ = (forward * -Math.cos(yaw) + strafe * Math.sin(yaw)) * multiplier;

        mc.player.setVelocity(motionX, mc.player.getVelocity().y, motionZ);

        // Mix position and no-position packets
        if (tickCounter % 3 == 0) {
            sendPositionPacket(true);
        } else {
            sendNoPositionPacket();
        }
    }

    /**
     * BHOP MODE
     * Bunny hop with auto-jump and speed boost
     */
    private void applyBhop() {
        if (mc.player == null) return;

        if (mc.player.isOnGround()) {
            if (autoJump.getVal() && isMoving()) {
                mc.player.jump();
            }

            // Boost on ground
            Vec3d velocity = mc.player.getVelocity();
            double multiplier = speed.getVal() * 0.15;
            mc.player.setVelocity(velocity.x * multiplier, velocity.y, velocity.z * multiplier);
        }

        // Send packets
        if (mc.player.isOnGround()) {
            sendPositionPacket(true);
        } else {
            sendNoPositionPacket();
        }
    }

    /**
     * VANILLA MODE
     * Simple speed multiplier with normal packets
     */
    private void applyVanilla() {
        if (mc.player == null) return;
        if (onlyOnGround.getVal() && !mc.player.isOnGround()) return;

        Vec3d velocity = mc.player.getVelocity();
        double multiplier = speed.getVal() * 0.1;

        mc.player.setVelocity(
            velocity.x * multiplier,
            velocity.y,
            velocity.z * multiplier
        );
    }

    // ============================================
    // PACKET HELPERS
    // ============================================

    /**
     * Send PLAYER_FLYING packet with NO POSITION DATA
     * This triggers the 0.03 stacking vulnerability in GrimAC
     */
    private void sendNoPositionPacket() {
        if (mc.player == null) return;

        // OnGroundOnly packet - FIXED: needs two boolean parameters in 1.21.11
        boolean onGround = mc.player.isOnGround();
        mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.OnGroundOnly(onGround, onGround)
        );

        wasOnGround = onGround;
    }

    /**
     * Send full position packet to sync with server
     */
    private void sendPositionPacket(boolean updateLast) {
        if (mc.player == null) return;

        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();
        boolean onGround = mc.player.isOnGround();

        // Send full position + rotation packet - FIXED: needs 7 parameters in 1.21.11
        mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, onGround, onGround)
        );

        if (updateLast) {
            lastX = x;
            lastY = y;
            lastZ = z;
            lastYaw = yaw;
            lastPitch = pitch;
            wasOnGround = onGround;
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private boolean isMoving() {
        return mc.options.forwardKey.isPressed() ||
               mc.options.backKey.isPressed() ||
               mc.options.leftKey.isPressed() ||
               mc.options.rightKey.isPressed();
    }
}