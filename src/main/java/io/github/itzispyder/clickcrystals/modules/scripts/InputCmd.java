package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;

import java.util.function.BooleanSupplier;

public class InputCmd extends ScriptCommand implements Global {

    public InputCmd() {
        super("input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        args.get(0).enumValue(Action.class, null).run();
    }

    public enum Action {
        ATTACK(InteractionUtils::doAttack, mc.options.attackKey::isPressed),
        USE(InteractionUtils::doUse, mc.options.useKey::isPressed),
        FORWARD(() -> TickEventListener.forward(500), mc.options.forwardKey::isPressed),
        BACKWARD(() -> TickEventListener.backward(500), mc.options.backKey::isPressed),
        STRAFE_LEFT(() -> TickEventListener.strafeLeft(500), mc.options.leftKey::isPressed),
        STRAFE_RIGHT(() -> TickEventListener.strafeRight(500), mc.options.rightKey::isPressed),
        SNEAK(() -> TickEventListener.sneak(500), mc.options.sneakKey::isPressed),
        LOCK_CURSOR(CameraRotator::lockCursor, CameraRotator::isCursorLocked),
        UNLOCK_CURSOR(CameraRotator::unlockCursor, () -> !CameraRotator.isCursorLocked());

        private final Runnable action;
        private final BooleanSupplier isActive;

        Action(Runnable action, BooleanSupplier isActive) {
            this.action = action;
            this.isActive = isActive;
        }

        public boolean isActive() {
            return isActive.getAsBoolean();
        }

        public void run() {
            if (mc != null && mc.options != null) {
                action.run();
            }
        }
    }
}
