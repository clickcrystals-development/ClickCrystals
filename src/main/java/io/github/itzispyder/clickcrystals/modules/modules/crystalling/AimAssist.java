package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class AimAssist extends Module implements Listener {

    // ============================================
    // SETTING SECTIONS
    // ============================================

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scTargeting = createSettingSection("targeting");
    private final SettingSection scSpeed = createSettingSection("speed");
    private final SettingSection scBehavior = createSettingSection("behavior");
    private final SettingSection scBypass = createSettingSection("bypass");

    // ============================================
    // GENERAL SETTINGS
    // ============================================

    private final ModuleSetting<String> weaponMode = scGeneral.add(createStringSetting()
            .name("weapon-filter")
            .description("Which items trigger aim assist: MACE_ONLY, WEAPONS_ONLY, MACE_AND_WEAPONS, ALL")
            .def("WEAPONS_ONLY")
            .build()
    );

    private final ModuleSetting<Boolean> onLeftClick = scGeneral.add(createBoolSetting()
            .name("on-left-click")
            .description("Only works when holding down left click")
            .def(false)
            .build()
    );

    private final ModuleSetting<String> lerpMode = scGeneral.add(createStringSetting()
            .name("lerp-mode")
            .description("Smoothing algorithm: NORMAL, SMOOTHSTEP, EASEOUT")
            .def("NORMAL")
            .build()
    );

    // ============================================
    // TARGETING SETTINGS
    // ============================================

    private final ModuleSetting<String> aimAt = scTargeting.add(createStringSetting()
            .name("aim-at")
            .description("Which part to aim at: HEAD, CHEST, LEGS")
            .def("HEAD")
            .build()
    );

    private final ModuleSetting<Double> radius = scTargeting.add(createDoubleSetting()
            .name("radius")
            .description("Range to search for targets")
            .def(5.0)
            .min(0.1)
            .max(6.0)
            .build()
    );

    private final ModuleSetting<Double> fov = scTargeting.add(createDoubleSetting()
            .name("fov")
            .description("Field of view angle for targeting")
            .def(100.0)
            .min(5.0)
            .max(360.0)
            .build()
    );

    private final ModuleSetting<Boolean> seeOnly = scTargeting.add(createBoolSetting()
            .name("see-only")
            .description("Only target visible players")
            .def(true)
            .build()
    );

    private final ModuleSetting<Boolean> stickyAim = scTargeting.add(createBoolSetting()
            .name("sticky-aim")
            .description("Keeps aiming at last attacked player")
            .def(false)
            .build()
    );

    private final ModuleSetting<Boolean> lookAtNearest = scTargeting.add(createBoolSetting()
            .name("look-at-nearest")
            .description("Aims at nearest edge of player hitbox")
            .def(false)
            .build()
    );

    // ============================================
    // SPEED SETTINGS
    // ============================================

    private final ModuleSetting<Double> yawSpeed = scSpeed.add(createDoubleSetting()
            .name("horizontal-speed")
            .description("Horizontal rotation speed (randomized)")
            .def(3.0)
            .min(0.0)
            .max(10.0)
            .build()
    );

    private final ModuleSetting<Double> pitchSpeed = scSpeed.add(createDoubleSetting()
            .name("vertical-speed")
            .description("Vertical rotation speed (randomized)")
            .def(3.0)
            .min(0.0)
            .max(10.0)
            .build()
    );

    private final ModuleSetting<Double> speedChange = scSpeed.add(createDoubleSetting()
            .name("speed-delay")
            .description("Time in ms to wait before resetting random speed")
            .def(250.0)
            .min(0.0)
            .max(1000.0)
            .build()
    );

    // ============================================
    // BEHAVIOR SETTINGS
    // ============================================

    private final ModuleSetting<Boolean> yawAssist = scBehavior.add(createBoolSetting()
            .name("horizontal-assist")
            .description("Enable horizontal rotation assist")
            .def(true)
            .build()
    );

    private final ModuleSetting<Boolean> pitchAssist = scBehavior.add(createBoolSetting()
            .name("vertical-assist")
            .description("Enable vertical rotation assist")
            .def(true)
            .build()
    );

    private final ModuleSetting<Double> randomization = scBehavior.add(createDoubleSetting()
            .name("chance")
            .description("Percentage chance to apply rotation each tick")
            .def(50.0)
            .min(0.0)
            .max(100.0)
            .build()
    );

    private final ModuleSetting<Double> waitFor = scBehavior.add(createDoubleSetting()
            .name("wait-on-move")
            .description("Delay in ms after mouse movement to resume aiming")
            .def(0.0)
            .min(0.0)
            .max(1000.0)
            .build()
    );

    // ============================================
    // BYPASS SETTINGS
    // ============================================

    private final ModuleSetting<Boolean> stopAtTargetVertical = scBypass.add(createBoolSetting()
            .name("stop-at-target-vertical")
            .description("Stops vertical aim if already looking at target (anti-cheat bypass)")
            .def(true)
            .build()
    );

    private final ModuleSetting<Boolean> stopAtTargetHorizontal = scBypass.add(createBoolSetting()
            .name("stop-at-target-horizontal")
            .description("Stops horizontal aim if already looking at target (anti-cheat bypass)")
            .def(false)
            .build()
    );

    // ============================================
    // INTERNAL FIELDS
    // ============================================

    private final InternalTimer timer = new InternalTimer();
    private final InternalTimer resetSpeed = new InternalTimer();
    private final InternalTimer mouseTimer = new InternalTimer();
    private final Random random = new Random(System.currentTimeMillis());

    private boolean canMove = true;
    private float currentPitchSpeed;
    private float currentYawSpeed;
    private double lastMouseX = 0;
    private double lastMouseY = 0;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public AimAssist() {
        super("aim-assist", Categories.CRYSTAL, "Automatically aims at players for you");
    }

    // ============================================
    // MODULE LIFECYCLE
    // ============================================

    @Override
    protected void onEnable() {
        system.addListener(this);
        canMove = true;
        resetRandomSpeeds();
        timer.reset();
        mouseTimer.reset();
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    // ============================================
    // EVENT HANDLERS
    // ============================================

    @EventHandler
    private void onClientTick(ClientTickEndEvent e) {
        // Mouse movement detection
        if (mc.mouse != null) {
            double currentX = mc.mouse.getX();
            double currentY = mc.mouse.getY();

            if (currentX != lastMouseX || currentY != lastMouseY) {
                canMove = false;
                mouseTimer.reset();
                lastMouseX = currentX;
                lastMouseY = currentY;
            }
        }

        // Reset movement flag after wait time
        if (mouseTimer.delay(waitFor.getVal().floatValue()) && !canMove) {
            canMove = true;
            mouseTimer.reset();
        }

        // Basic validation
        if (mc.player == null || mc.currentScreen != null) return;
        if (!isValidWeapon()) return;
        if (onLeftClick.getVal() && !isLeftClickPressed()) return;

        // Find target
        PlayerEntity target = findNearestPlayer(mc.player, radius.getVal().floatValue(), seeOnly.getVal());

        // Sticky aim
        Entity attacking = mc.player.getAttacking();
        if (stickyAim.getVal() && attacking instanceof PlayerEntity player && player.distanceTo(mc.player) < radius.getVal())
            target = player;

        if (target == null) return;

        // Reset random speeds periodically
        if (resetSpeed.delay(speedChange.getVal().floatValue())) {
            resetRandomSpeeds();
            resetSpeed.reset();
        }

        // Get target position
        Vec3d targetPos = getTargetPosition(target);
        targetPos = adjustAimPosition(targetPos);

        if (lookAtNearest.getVal())
            targetPos = applyNearestOffset(targetPos, target);

        // Calculate rotation
        Rotation rotation = getDirection(mc.player, targetPos);

        // FOV check
        if (getAngleToRotation(rotation) > fov.getVal() / 2) return;

        // Apply lerp
        float yawStrength = currentYawSpeed / 50f;
        float pitchStrength = currentPitchSpeed / 50f;
        float newYaw = applyLerp(yawStrength, mc.player.getYaw(), (float) rotation.yaw());
        float newPitch = applyLerp(pitchStrength, mc.player.getPitch(), (float) rotation.pitch());

        // Apply rotation
        if (shouldApplyRotation() && canMove) {
            applyYawRotation(newYaw, target);
            applyPitchRotation(newPitch, target);
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private boolean isValidWeapon() {
        Item heldItem = mc.player.getMainHandStack().getItem();
        String mode = weaponMode.getVal().toUpperCase();

        return switch (mode) {
            case "MACE_ONLY" -> heldItem instanceof MaceItem;
            case "WEAPONS_ONLY" -> isWeapon(heldItem);
            case "MACE_AND_WEAPONS" -> isWeapon(heldItem) || heldItem instanceof MaceItem;
            default -> true;
        };
    }

    private boolean isWeapon(Item item) {
        if (item instanceof AxeItem) return true;
        String itemName = item.toString().toLowerCase();
        return itemName.contains("sword");
    }

    private boolean isLeftClickPressed() {
        return GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
    }

    private Vec3d getTargetPosition(PlayerEntity target) {
        return new Vec3d(target.getX(), target.getY(), target.getZ());
    }

    private Vec3d adjustAimPosition(Vec3d targetPos) {
        String mode = aimAt.getVal().toUpperCase();
        return switch (mode) {
            case "CHEST" -> targetPos.add(0, -0.5, 0);
            case "LEGS" -> targetPos.add(0, -1.2, 0);
            default -> targetPos;
        };
    }

    private Vec3d applyNearestOffset(Vec3d targetPos, PlayerEntity target) {
        double offsetX = mc.player.getX() - target.getX() > 0 ? 0.29 : -0.29;
        double offsetZ = mc.player.getZ() - target.getZ() > 0 ? 0.29 : -0.29;
        return targetPos.add(offsetX, 0, offsetZ);
    }

    private float applyLerp(float strength, float current, float target) {
        String mode = lerpMode.getVal().toUpperCase();
        return switch (mode) {
            case "SMOOTHSTEP" -> (float) smoothStepLerp(strength, current, target);
            case "EASEOUT" -> (float) easeOutBackDegrees(current, target, strength * 0.05f);
            default -> lerpRotation(strength, current, target);
        };
    }

    private boolean shouldApplyRotation() {
        return randomInt(1, 100) <= randomization.getVal();
    }

    private void applyYawRotation(float newYaw, PlayerEntity target) {
        if (!yawAssist.getVal()) return;
        if (stopAtTargetHorizontal.getVal() && isLookingAtTarget(target)) return;
        mc.player.setYaw(newYaw);
    }

    private void applyPitchRotation(float newPitch, PlayerEntity target) {
        if (!pitchAssist.getVal()) return;
        if (stopAtTargetVertical.getVal() && isLookingAtTarget(target)) return;
        mc.player.setPitch(newPitch);
    }

    private void resetRandomSpeeds() {
        currentYawSpeed = (float) (yawSpeed.getVal() + (random.nextDouble() - 0.5) * 2);
        currentPitchSpeed = (float) (pitchSpeed.getVal() + (random.nextDouble() - 0.5) * 2);
    }

    // ============================================
    // TARGETING UTILITIES
    // ============================================

    private PlayerEntity findNearestPlayer(ClientPlayerEntity fromPlayer, float range, boolean seeOnly) {
        float minRange = Float.MAX_VALUE;
        PlayerEntity minPlayer = null;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == fromPlayer) continue;

            Vec3d fromPos = new Vec3d(fromPlayer.getX(), fromPlayer.getY(), fromPlayer.getZ());
            Vec3d playerPos = new Vec3d(player.getX(), player.getY(), player.getZ());
            float distance = (float) distance(fromPos, playerPos);

            if (distance <= range && (!seeOnly || player.canSee(fromPlayer))) {
                if (distance < minRange) {
                    minRange = distance;
                    minPlayer = player;
                }
            }
        }

        return minPlayer;
    }

    private double distance(Vec3d from, Vec3d to) {
        return Math.sqrt(Math.pow(to.x - from.x, 2) + Math.pow(to.y - from.y, 2) + Math.pow(to.z - from.z, 2));
    }

    private boolean isLookingAtTarget(Entity target) {
        HitResult result = getHitResult(radius.getVal());
        return result instanceof EntityHitResult hitResult && hitResult.getEntity() == target;
    }

    private HitResult getHitResult(double distance) {
        if (mc.player == null || mc.world == null) return null;

        Vec3d cameraPosVec = mc.player.getCameraPosVec(1.0f);
        Vec3d rotationVec = getPlayerLookVec(mc.player.getYaw(), mc.player.getPitch());
        Vec3d range = cameraPosVec.add(rotationVec.x * distance, rotationVec.y * distance, rotationVec.z * distance);

        HitResult result = mc.world.raycast(new RaycastContext(
            cameraPosVec, range,
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            mc.player
        ));

        double maxDistSq = distance * distance;
        if (result != null)
            maxDistSq = result.getPos().squaredDistanceTo(cameraPosVec);

        Vec3d vec3d3 = cameraPosVec.add(rotationVec.x * distance, rotationVec.y * distance, rotationVec.z * distance);
        Box box = mc.player.getBoundingBox().stretch(rotationVec.multiply(distance)).expand(1.0, 1.0, 1.0);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(
            mc.player, cameraPosVec, vec3d3, box,
            (entity) -> !entity.isSpectator() && entity.canHit(),
            maxDistSq
        );

        if (entityHitResult != null) {
            Vec3d hitPos = entityHitResult.getPos();
            double distSq = cameraPosVec.squaredDistanceTo(hitPos);
            if (distSq < maxDistSq || result == null)
                result = entityHitResult;
        }

        return result;
    }

    // ============================================
    // ROTATION UTILITIES
    // ============================================

    private Rotation getDirection(Entity entity, Vec3d vec) {
        double dx = vec.x - entity.getX();
        double dy = vec.y - entity.getY();
        double dz = vec.z - entity.getZ();
        double dist = MathHelper.sqrt((float) (dx * dx + dz * dz));

        return new Rotation(
            MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dz, dx)) - 90.0),
            -MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dy, dist)))
        );
    }

    private double getAngleToRotation(Rotation rotation) {
        double currentYaw = MathHelper.wrapDegrees(mc.player.getYaw());
        double currentPitch = MathHelper.wrapDegrees(mc.player.getPitch());
        double diffYaw = MathHelper.wrapDegrees(currentYaw - rotation.yaw());
        double diffPitch = MathHelper.wrapDegrees(currentPitch - rotation.pitch());
        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    private Vec3d getPlayerLookVec(float yaw, float pitch) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((i * j), (-k), (h * j));
    }

    // ============================================
    // INTERPOLATION METHODS
    // ============================================

    private float lerpRotation(float delta, float start, float end) {
        return start + (MathHelper.wrapDegrees(end - start) * delta);
    }

    private double smoothStepLerp(double delta, double start, double end) {
        delta = Math.max(0, Math.min(1, delta));
        double t = delta * delta * (3 - 2 * delta);
        return start + MathHelper.wrapDegrees(end - start) * t;
    }

    private double easeOutBackDegrees(double start, double end, float speed) {
        double c1 = 1.70158;
        double c3 = 2.70158;
        double x = 1 - Math.pow(1 - (double) speed, 3);
        return start + MathHelper.wrapDegrees(end - start) * (1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2));
    }

    // ============================================
    // MATH UTILITIES
    // ============================================

    private int randomInt(int start, int bound) {
        if (start >= bound) return start;
        return random.nextInt(bound - start + 1) + start;
    }

    // ============================================
    // INTERNAL CLASSES
    // ============================================

    private static class InternalTimer {
        private long lastMS;

        public InternalTimer() {
            reset();
        }

        public long getCurrentMS() {
            return System.nanoTime() / 1000000L;
        }

        public void reset() {
            this.lastMS = getCurrentMS();
        }

        public boolean delay(float milliSec) {
            return getCurrentMS() - lastMS >= milliSec;
        }
    }

    public record Rotation(double yaw, double pitch) {}
}