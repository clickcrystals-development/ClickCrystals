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
import io.github.itzispyder.clickcrystals.scripting.runtime.InputLeaseManager;

public class TickEventListener implements Listener, Global {

    public static boolean shouldForward, shouldBackward, shouldStrafeLeft, shouldStrafeRight, shouldSneak, shouldJump;
    public static boolean shouldAttack, shouldUse;
    private static final ConcurrentLinkedQueue<Integer> heldKeys = new ConcurrentLinkedQueue<>();
    // A stale delayed release must never cancel a newer hold after reset.
    // One generation per action allows independent simultaneous holds.
    private static final InputLeaseManager inputLeases = new InputLeaseManager();

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
        // Cancel the script-owned button state and IMMEDIATELY release only
        // buttons that this automation had pressed. Without the release,
        // a previously synthesized use/attack may remain pressed in Options.
        boolean forward = shouldForward;
        boolean backward = shouldBackward;
        boolean left = shouldStrafeLeft;
        boolean right = shouldStrafeRight;
        boolean sneak = shouldSneak;
        boolean jump = shouldJump;
        boolean attack = shouldAttack;
        boolean use = shouldUse;
        long[] cancelled = inputLeases.cancelAllButtons();
        shouldForward = shouldBackward = shouldStrafeLeft = shouldStrafeRight =
                shouldSneak = shouldJump = shouldAttack = shouldUse = false;
        heldKeys.clear();
        inputLeases.cancelAllKeys();
        if (mc != null && mc.options != null) {
            mc.execute(() -> {
                // A newer script hold may have started before this runs.
                // Do not release the new owner's key press.
                if (forward && inputLeases.ownsButton(0, cancelled[0])) mc.options.forwardKey.setPressed(false);
                if (backward && inputLeases.ownsButton(1, cancelled[1])) mc.options.backKey.setPressed(false);
                if (left && inputLeases.ownsButton(2, cancelled[2])) mc.options.leftKey.setPressed(false);
                if (right && inputLeases.ownsButton(3, cancelled[3])) mc.options.rightKey.setPressed(false);
                if (sneak && inputLeases.ownsButton(4, cancelled[4])) mc.options.sneakKey.setPressed(false);
                if (jump && inputLeases.ownsButton(5, cancelled[5])) mc.options.jumpKey.setPressed(false);
                if (attack && inputLeases.ownsButton(6, cancelled[6])) mc.options.attackKey.setPressed(false);
                if (use && inputLeases.ownsButton(7, cancelled[7])) mc.options.useKey.setPressed(false);
            });
        }
    }

    public static void forward(long millis) {
        if (!shouldForward) {
            shouldForward = true;
            long generation = inputLeases.acquireButton(0);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(0, generation)) return;
                shouldForward = false;
                mc.options.forwardKey.setPressed(false);
            }), millis);
        }
    }

    public static void backward(long millis) {
        if (!shouldBackward) {
            shouldBackward = true;
            long generation = inputLeases.acquireButton(1);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(1, generation)) return;
                shouldBackward = false;
                mc.options.backKey.setPressed(false);
            }), millis);
        }
    }

    public static void strafeLeft(long millis) {
        if (!shouldStrafeLeft) {
            shouldStrafeLeft = true;
            long generation = inputLeases.acquireButton(2);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(2, generation)) return;
                shouldStrafeLeft = false;
                mc.options.leftKey.setPressed(false);
            }), millis);
        }
    }

    public static void strafeRight(long millis) {
        if (!shouldStrafeRight) {
            shouldStrafeRight = true;
            long generation = inputLeases.acquireButton(3);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(3, generation)) return;
                shouldStrafeRight = false;
                mc.options.rightKey.setPressed(false);
            }), millis);
        }
    }

    public static void sneak(long millis) {
        if (!shouldSneak) {
            shouldSneak = true;
            long generation = inputLeases.acquireButton(4);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(4, generation)) return;
                shouldSneak = false;
                mc.options.sneakKey.setPressed(false);
            }), millis);
        }
    }

    public static void jump(long millis) {
        if (!shouldJump) {
            shouldJump = true;
            long generation = inputLeases.acquireButton(5);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(5, generation)) return;
                shouldJump = false;
                mc.options.jumpKey.setPressed(false);
            }), millis);
        }
    }

    public static void attack(long millis) {
        if (!shouldAttack) {
            shouldAttack = true;
            long generation = inputLeases.acquireButton(6);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(6, generation)) return;
                shouldAttack = false;
                mc.options.attackKey.setPressed(false);
            }), millis);
        }
    }

    public static void use(long millis) {
        if (!shouldUse) {
            shouldUse = true;
            long generation = inputLeases.acquireButton(7);
            system.scheduler.runDelayedTask(() -> mc.execute(() -> {
                if (!inputLeases.ownsButton(7, generation)) return;
                shouldUse = false;
                mc.options.useKey.setPressed(false);
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
        long lease = inputLeases.acquireKey(keyCode);
        if (lease == -1) return;
        heldKeys.add(keyCode);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            if (inputLeases.releaseKey(keyCode, lease)) heldKeys.remove(keyCode);
        }), millis);
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

        for (int heldKey: heldKeys)
            ((AccessorKeyboard) mc.keyboard).pressKey(heldKey, 42);
    }
}
