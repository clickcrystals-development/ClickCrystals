package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;

import java.util.function.BooleanSupplier;

public class InputCmd extends ScriptCommand implements Global {

    public InputCmd() {
        super("input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        args.get(0).enumValue(Action.class, null).run();

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }

    public enum Action {
        ATTACK(InteractionUtils::inputAttack, mc.options.attackKey::isPressed),
        USE(InteractionUtils::inputUse, mc.options.useKey::isPressed),
        FORWARD(InteractionUtils::inputForward, mc.options.forwardKey::isPressed),
        BACKWARD(InteractionUtils::inputBackward, mc.options.backKey::isPressed),
        STRAFE_LEFT(InteractionUtils::inputStrafeLeft, mc.options.leftKey::isPressed),
        STRAFE_RIGHT(InteractionUtils::inputStrafeRight, mc.options.rightKey::isPressed),
        JUMP(InteractionUtils::inputJump, mc.options.jumpKey::isPressed),
        SNEAK(InteractionUtils::inputSneak, mc.options.sneakKey::isPressed),
        LOCK_CURSOR(CameraRotator::lockCursor, CameraRotator::isCursorLocked),
        UNLOCK_CURSOR(CameraRotator::unlockCursor, () -> !CameraRotator.isCursorLocked()),
        GUI_LEFT(InteractionUtils::leftClick, mc.mouse::wasLeftButtonClicked),
        GUI_RIGHT(InteractionUtils::rightClick, mc.mouse::wasRightButtonClicked),
        GUI_MIDDLE(InteractionUtils::middleClick, mc.mouse::wasMiddleButtonClicked);

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
