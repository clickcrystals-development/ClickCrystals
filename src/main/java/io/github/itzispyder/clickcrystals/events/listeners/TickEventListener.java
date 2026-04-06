package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorKeyboard;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TickEventListener implements Listener, Global {

    public static boolean shouldForward, shouldBackward, shouldStrafeLeft, shouldStrafeRight, shouldSneak, shouldJump;
    public static boolean shouldAttack, shouldUse;
    private static final ConcurrentLinkedQueue<Integer> heldKeys = new ConcurrentLinkedQueue<>();

    @EventHandler
    public void onTickStart(ClientTickStartEvent e) {
        try {
            this.handleAutoKeys();
        }
        catch (Exception ignore) {};
    }

    @EventHandler
    public void onTickEnd(ClientTickEndEvent e) {
        system.cameraRotator.onGameTick();
    }

    @EventHandler
    public void onRenderTick(RenderWorldEvent e) {
        system.cameraRotator.onRenderTick();
    }

    public static void cancelTickInputs() {
        shouldForward = shouldBackward = shouldStrafeLeft = shouldStrafeRight =
                shouldSneak = shouldJump = shouldAttack = shouldUse = false;
        heldKeys.clear();
    }

    public static void forward(long millis) {
        if (!shouldForward) {
            shouldForward = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldForward = false;
                mc.options.keyUp.setDown(false);
            }), millis);
        }
    }

    public static void backward(long millis) {
        if (!shouldBackward) {
            shouldBackward = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldBackward = false;
                mc.options.keyDown.setDown(false);
            }), millis);
        }
    }

    public static void strafeLeft(long millis) {
        if (!shouldStrafeLeft) {
            shouldStrafeLeft = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldStrafeLeft = false;
                mc.options.keyLeft.setDown(false);
            }), millis);
        }
    }

    public static void strafeRight(long millis) {
        if (!shouldStrafeRight) {
            shouldStrafeRight = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldStrafeRight = false;
                mc.options.keyRight.setDown(false);
            }), millis);
        }
    }

    public static void sneak(long millis) {
        if (!shouldSneak) {
            shouldSneak = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldSneak = false;
                mc.options.keyShift.setDown(false);
            }), millis);
        }
    }

    public static void jump(long millis) {
        if (!shouldJump) {
            shouldJump = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldJump = false;
                mc.options.keyJump.setDown(false);
            }), millis);
        }
    }

    public static void attack(long millis) {
        if (!shouldAttack) {
            shouldAttack = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldAttack = false;
                mc.options.keyAttack.setDown(false);
            }), millis);
        }
    }

    public static void use(long millis) {
        if (!shouldUse) {
            shouldUse = true;
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                shouldUse = false;
                mc.options.keyUse.setDown(false);
            }), millis);
        }
    }

    public static void holdKey(String keyName, long millis) {
        int keyCode = Keybind.fromExtendedKeyName(keyName);
        if (keyCode != -1) {
            holdKey(keyCode, millis);
        }
    }

    public static void holdKey(int keyCode, long millis) {
        if (heldKeys.contains(keyCode))
            return;

        heldKeys.add(keyCode);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            heldKeys.remove(keyCode);
        }), millis);
    }

    private void handleAutoKeys() {
        if (shouldForward) {
            mc.options.keyUp.setDown(true);
        }
        if (shouldBackward) {
            mc.options.keyDown.setDown(true);
        }
        if (shouldStrafeLeft) {
            mc.options.keyLeft.setDown(true);
        }
        if (shouldStrafeRight) {
            mc.options.keyRight.setDown(true);
        }
        if (shouldSneak) {
            mc.options.keyShift.setDown(true);
        }
        if (shouldJump) {
            mc.options.keyJump.setDown(true);
        }
        if (shouldAttack) {
            mc.options.keyAttack.setDown(true);
        }
        if (shouldUse) {
            mc.options.keyUse.setDown(true);
        }

        for (int heldKey: heldKeys)
            ((AccessorKeyboard) mc.keyboardHandler).pressKey(heldKey, 42);
    }
}
