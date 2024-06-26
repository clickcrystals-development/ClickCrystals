package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;

public class InputCmd extends ScriptCommand implements Global, ThenChainable {

    public InputCmd() {
        super("input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        Action a = args.get(0).toEnum(Action.class, null);
        if (a != Action.KEY) {
            a.run();
            executeWithThen(args, 1);
        }
        else {
            InteractionUtils.pressKeyExtendedName(args.get(1).toString());
            executeWithThen(args, 2);
        }
    }

    public enum Action {
        ATTACK(InteractionUtils::inputAttack, InteractionUtils::inputAttack, mc.options.attackKey::isPressed),
        USE(InteractionUtils::inputUse, InteractionUtils::inputUse, mc.options.useKey::isPressed),
        FORWARD(InteractionUtils::inputForward, InteractionUtils::inputForward, mc.options.forwardKey::isPressed),
        BACKWARD(InteractionUtils::inputBackward, InteractionUtils::inputBackward, mc.options.backKey::isPressed),
        STRAFE_LEFT(InteractionUtils::inputStrafeLeft, InteractionUtils::inputStrafeLeft, mc.options.leftKey::isPressed),
        STRAFE_RIGHT(InteractionUtils::inputStrafeRight, InteractionUtils::inputStrafeRight, mc.options.rightKey::isPressed),
        JUMP(InteractionUtils::inputJump, InteractionUtils::inputJump, mc.options.jumpKey::isPressed),
        SPRINT(InteractionUtils::inputToggleSprint, null, mc.options.sprintKey::isPressed),
        SNEAK(InteractionUtils::inputSneak, null, mc.options.sneakKey::isPressed),
        LOCK_CURSOR(CameraRotator::lockCursor, null, CameraRotator::isCursorLocked),
        UNLOCK_CURSOR(CameraRotator::unlockCursor, null, () -> !CameraRotator.isCursorLocked()),
        LEFT(InteractionUtils::leftClick, null, mc.mouse::wasLeftButtonClicked),
        RIGHT(InteractionUtils::rightClick, null, mc.mouse::wasRightButtonClicked),
        MIDDLE(InteractionUtils::middleClick, null, mc.mouse::wasMiddleButtonClicked),
        INVENTORY(InteractionUtils::inputInventory, null, () -> mc.currentScreen instanceof HandledScreen<?>),
        KEY(null, null, null);

        private final Runnable action;
        private final LongConsumer holdAction;
        private final BooleanSupplier isActive;

        Action(Runnable action, LongConsumer holdAction, BooleanSupplier isActive) {
            this.action = action;
            this.isActive = isActive;
            this.holdAction = holdAction;
        }

        public boolean isActive() {
            return !isDummy() && isActive.getAsBoolean();
        }

        public boolean isDummy() {
            return action == null || isActive == null;
        }

        public void run() {
            if (!isDummy() && mc != null && mc.options != null) {
                action.run();
            }
        }

        public void runFor(long ms) {
            if (!isDummy() && mc != null && mc.options != null) {
                if (holdAction != null)
                    holdAction.accept(ms);
                else
                    run();
            }
        }
    }
}
