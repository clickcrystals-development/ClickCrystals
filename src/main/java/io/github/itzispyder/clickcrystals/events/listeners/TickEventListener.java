package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;

public class TickEventListener implements Listener, Global {

    public static boolean shouldForward, shouldBackward, shouldStrafeLeft, shouldStrafeRight, shouldSneak, shouldJump;
    public static boolean shouldAttack, shouldUse;

    @EventHandler
    public void onTickStart(ClientTickStartEvent e) {
        try {
            this.handleAutoKeys();
        }
        catch (Exception ignore) {};
    }

    @EventHandler
    public void onTickEnd(ClientTickEndEvent e) {

    }

    public static void cancelTickInputs() {
        shouldForward = shouldBackward = shouldStrafeLeft = shouldStrafeRight =
                shouldSneak = shouldJump = shouldAttack = shouldUse = false;
    }

    public static void forward(long millis) {
        if (!shouldForward) {
            shouldForward = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldForward = false;
                mc.options.forwardKey.setPressed(false);
            }), millis);
        }
    }

    public static void backward(long millis) {
        if (!shouldBackward) {
            shouldBackward = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldBackward = false;
                mc.options.backKey.setPressed(false);
            }), millis);
        }
    }

    public static void strafeLeft(long millis) {
        if (!shouldStrafeLeft) {
            shouldStrafeLeft = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldStrafeLeft = false;
                mc.options.leftKey.setPressed(false);
            }), millis);
        }
    }

    public static void strafeRight(long millis) {
        if (!shouldStrafeRight) {
            shouldStrafeRight = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldStrafeRight = false;
                mc.options.rightKey.setPressed(false);
            }), millis);
        }
    }

    public static void sneak(long millis) {
        if (!shouldSneak) {
            shouldSneak = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldSneak = false;
                mc.options.sneakKey.setPressed(false);
            }), millis);
        }
    }

    public static void jump(long millis) {
        if (!shouldJump) {
            shouldJump = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldJump = false;
                mc.options.jumpKey.setPressed(false);
            }), millis);
        }
    }

    public static void attack(long millis) {
        if (!shouldAttack) {
            shouldAttack = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldAttack = false;
                mc.options.attackKey.setPressed(false);
            }), millis);
        }
    }

    public static void use(long millis) {
        if (!shouldUse) {
            shouldUse = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldUse = false;
                mc.options.useKey.setPressed(false);
            }), millis);
        }
    }

    private void handleAutoKeys() {
        if (shouldForward) {
            mc.options.forwardKey.setPressed(true);
        }
        if (shouldBackward) {
            mc.options.backKey.setPressed(true);
        }
        if (shouldStrafeLeft) {
            mc.options.leftKey.setPressed(true);
        }
        if (shouldStrafeRight) {
            mc.options.rightKey.setPressed(true);
        }
        if (shouldSneak) {
            mc.options.sneakKey.setPressed(true);
        }
        if (shouldJump) {
            mc.options.jumpKey.setPressed(true);
        }
        if (shouldAttack) {
            mc.options.attackKey.setPressed(true);
        }
        if (shouldUse) {
            mc.options.useKey.setPressed(true);
        }
    }
}
