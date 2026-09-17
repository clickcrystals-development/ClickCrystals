package io.github.itzispyder.clickcrystals.scripting.syntax.macros.camera;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
import io.github.itzispyder.clickcrystals.scripting.syntax.AimAnchorType;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class TurnOptions {

    private static final Random RNG = new Random();

    public AimAnchorType anchor;
    public float speed;
    public RotationProfile profile;

    private static float sensitivityGcd = 0.0f;
    private static float lastSampledYaw = Float.NaN;
    private static float lastSampledPitch = Float.NaN;
    private static long lastSampleMs = 0L;

    public TurnOptions() {
        anchor = AimAnchorType.FEET;
        speed = 10F;
        profile = RotationProfile.HUMAN_SMOOTH;
    }

    public Vec3 getCameraTicketPos(Vec3 camera, Vec3 dest) {
        return anchor.positionFactory.apply(dest).subtract(camera).normalize();
    }

    public void configure(ScriptArgsReader read) {
        if (read.currMatches("speed")) {
            read.next();
            speed = read.next().toFloat();
        } else if (read.currMatches("aim")) {
            read.next();
            anchor = read.next(AimAnchorType.class);
        } else if (read.currMatches("profile")) {
            read.next();
            try { profile = RotationProfile.valueOf(read.next().raw().toUpperCase()); }
            catch (Exception ignored) {}
        }
    }

    public static void sampleCurrentRotation() {
        var player = PlayerUtils.player();
        if (player == null) return;
        long now = System.currentTimeMillis();
        if (!Float.isNaN(lastSampledYaw) && now - lastSampleMs > 20L && now - lastSampleMs < 200L) {
            float dy = Math.abs(player.getYRot() - lastSampledYaw);
            float dp = Math.abs(player.getXRot() - lastSampledPitch);
            float raw = Math.max(dy, dp);
            if (raw > 0.001f) {
                sensitivityGcd = sensitivityGcd == 0.0f ? raw : gcd(sensitivityGcd, raw);
            }
        }
        lastSampledYaw = player.getYRot();
        lastSampledPitch = player.getXRot();
        lastSampleMs = now;
    }

    public static float snapToGcd(float delta) {
        if (sensitivityGcd < 0.0001f) return delta;
        float snapped = Math.round(delta / sensitivityGcd) * sensitivityGcd;
        float noise = (RNG.nextFloat() - 0.5f) * sensitivityGcd * 0.18f;
        return snapped + noise;
    }

    public static float applySubtickJitter(float delta) {
        if (Math.abs(delta) < 0.0001f) return delta;
        float jitter = (RNG.nextFloat() - 0.5f) * 0.012f;
        return delta + jitter;
    }

    public static float avoidExactInteger(float value) {
        float frac = value - (float) Math.floor(value);
        if (frac < 0.004f || frac > 0.996f) {
            value += (RNG.nextBoolean() ? 1 : -1) * (0.005f + RNG.nextFloat() * 0.018f);
        }
        return value;
    }

    private static float gcd(float a, float b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b > 0.0001f) {
            float t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    public static float getSensitivityGcd() { return sensitivityGcd; }

    public enum RotationProfile {
        HUMAN_SMOOTH,
        FAST_SNAP,
        CINEMATIC
    }
}
