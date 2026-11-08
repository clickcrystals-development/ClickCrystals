package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorKeyboardHandler;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

import java.util.concurrent.ConcurrentLinkedQueue;
import io.github.itzispyder.clickcrystals.scripting.runtime.InputLeaseManager;

public class TickEventListener implements Listener, Global {

    public static boolean shouldForward, shouldBackward, shouldStrafeLeft, shouldStrafeRight, shouldSneak, shouldJump;
    public static boolean shouldAttack, shouldUse;
    private static final ConcurrentLinkedQueue<Integer> heldKeys = new ConcurrentLinkedQueue<>();
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
    public void onRenderTick(RenderWorldEvent event) {
        system.cameraRotator.onRenderTick();
    }

    public static void cancelTickInputs() {
        // Release keys injected by the script runtime, not only their flags.
        boolean forward = shouldForward, backward = shouldBackward;
        boolean left = shouldStrafeLeft, right = shouldStrafeRight;
        boolean sneak = shouldSneak, jump = shouldJump;
        boolean attack = shouldAttack, use = shouldUse;
        long[] cancelled = inputLeases.cancelAllButtons();
        shouldForward = shouldBackward = shouldStrafeLeft = shouldStrafeRight =
                shouldSneak = shouldJump = shouldAttack = shouldUse = false;
        heldKeys.clear();
        inputLeases.cancelAllKeys();
        if (mc != null && mc.options != null) mc.execute(() -> {
            if (forward && inputLeases.ownsButton(0, cancelled[0])) mc.options.keyUp.setDown(false);
            if (backward && inputLeases.ownsButton(1, cancelled[1])) mc.options.keyDown.setDown(false);
            if (left && inputLeases.ownsButton(2, cancelled[2])) mc.options.keyLeft.setDown(false);
            if (right && inputLeases.ownsButton(3, cancelled[3])) mc.options.keyRight.setDown(false);
            if (sneak && inputLeases.ownsButton(4, cancelled[4])) mc.options.keyShift.setDown(false);
            if (jump && inputLeases.ownsButton(5, cancelled[5])) mc.options.keyJump.setDown(false);
            if (attack && inputLeases.ownsButton(6, cancelled[6])) mc.options.keyAttack.setDown(false);
            if (use && inputLeases.ownsButton(7, cancelled[7])) mc.options.keyUse.setDown(false);
        });
    }

    public static void cancelToggleInputs() {
        for (int key : UserInputListener.getPressedKeys())
            InteractionUtils.toggleKey(key, Keybind.DEFAULT_SCANCODE, false);
        for (InputType input : InputType.values())
            input.toggle(false);
    }

    public static void forward(long millis) {
        if (!shouldForward) {
            shouldForward = true;
            long generation = inputLeases.acquireButton(0);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(0, generation)) return;
                shouldForward = false;
                mc.options.keyUp.setDown(false);
            }), millis);
        }
    }

    public static void backward(long millis) {
        if (!shouldBackward) {
            shouldBackward = true;
            long generation = inputLeases.acquireButton(1);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(1, generation)) return;
                shouldBackward = false;
                mc.options.keyDown.setDown(false);
            }), millis);
        }
    }

    public static void strafeLeft(long millis) {
        if (!shouldStrafeLeft) {
            shouldStrafeLeft = true;
            long generation = inputLeases.acquireButton(2);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(2, generation)) return;
                shouldStrafeLeft = false;
                mc.options.keyLeft.setDown(false);
            }), millis);
        }
    }

    public static void strafeRight(long millis) {
        if (!shouldStrafeRight) {
            shouldStrafeRight = true;
            long generation = inputLeases.acquireButton(3);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(3, generation)) return;
                shouldStrafeRight = false;
                mc.options.keyRight.setDown(false);
            }), millis);
        }
    }

    public static void sneak(long millis) {
        if (!shouldSneak) {
            shouldSneak = true;
            long generation = inputLeases.acquireButton(4);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(4, generation)) return;
                shouldSneak = false;
                mc.options.keyShift.setDown(false);
            }), millis);
        }
    }

    public static void jump(long millis) {
        if (!shouldJump) {
            shouldJump = true;
            long generation = inputLeases.acquireButton(5);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(5, generation)) return;
                shouldJump = false;
                mc.options.keyJump.setDown(false);
            }), millis);
        }
    }

    public static void attack(long millis) {
        if (!shouldAttack) {
            shouldAttack = true;
            long generation = inputLeases.acquireButton(6);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(6, generation)) return;
                shouldAttack = false;
                mc.options.keyAttack.setDown(false);
            }), millis);
        }
    }

    public static void use(long millis) {
        if (!shouldUse) {
            shouldUse = true;
            long generation = inputLeases.acquireButton(7);
            system.scheduler.runDelayedTask(self -> mc.execute(() -> {
                if (!inputLeases.ownsButton(7, generation)) return;
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
        long lease = inputLeases.acquireKey(keyCode);
        if (lease == -1) return;
        heldKeys.add(keyCode);
        system.scheduler.runDelayedTask(self -> mc.execute(() -> {
            if (inputLeases.releaseKey(keyCode, lease)) heldKeys.remove(keyCode);
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
            ((AccessorKeyboardHandler) mc.keyboardHandler).clickCrystals$pressKey(heldKey, Keybind.DEFAULT_SCANCODE);
    }
}
