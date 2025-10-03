package io.github.itzispyder.clickcrystals.scripting.syntax;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;

public enum InputType implements Global {

    ATTACK(InteractionUtils::inputAttack, InteractionUtils::inputAttack, Global.mc.options.attackKey::isPressed),
    USE(InteractionUtils::inputUse, InteractionUtils::inputUse, Global.mc.options.useKey::isPressed),
    FORWARD(InteractionUtils::inputForward, InteractionUtils::inputForward, Global.mc.options.forwardKey::isPressed),
    BACKWARD(InteractionUtils::inputBackward, InteractionUtils::inputBackward, Global.mc.options.backKey::isPressed),
    STRAFE_LEFT(InteractionUtils::inputStrafeLeft, InteractionUtils::inputStrafeLeft, Global.mc.options.leftKey::isPressed),
    STRAFE_RIGHT(InteractionUtils::inputStrafeRight, InteractionUtils::inputStrafeRight, Global.mc.options.rightKey::isPressed),
    JUMP(InteractionUtils::inputJump, InteractionUtils::inputJump, Global.mc.options.jumpKey::isPressed),
    SPRINT(InteractionUtils::inputToggleSprint, null, Global.mc.options.sprintKey::isPressed),
    SNEAK(InteractionUtils::inputSneak, null, Global.mc.options.sneakKey::isPressed),
    LOCK_CURSOR(system.cameraRotator::lockCursor, null, system.cameraRotator::isCursorLocked),
    UNLOCK_CURSOR(system.cameraRotator::unlockCursor, null, () -> !system.cameraRotator.isCursorLocked()),
    LEFT(InteractionUtils::leftClick, null, Global.mc.mouse::wasLeftButtonClicked),
    RIGHT(InteractionUtils::rightClick, null, Global.mc.mouse::wasRightButtonClicked),
    MIDDLE(InteractionUtils::middleClick, null, Global.mc.mouse::wasMiddleButtonClicked),
    INVENTORY(InteractionUtils::inputInventory, null, () -> Global.mc.currentScreen instanceof HandledScreen<?>),
    MOUSE_WHEEL_UP(() -> InteractionUtils.mouseScroll(1), null, () -> false),
    MOUSE_WHEEL_DOWN(() -> InteractionUtils.mouseScroll(-1), null, () -> false),
    KEY(null, null, null);

    private final Runnable action;
    private final LongConsumer holdAction;
    private final BooleanSupplier isActive;

    InputType(Runnable action, LongConsumer holdAction, BooleanSupplier isActive) {
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
        if (!isDummy() && Global.mc != null && Global.mc.options != null) {
            action.run();
        }
    }

    public void runFor(long ms) {
        if (!isDummy() && Global.mc != null && Global.mc.options != null) {
            if (holdAction != null)
                holdAction.accept(ms);
            else
                run();
        }
    }
}
