package io.github.itzispyder.clickcrystals.scripting.syntax;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;

public enum InputType implements Global {

    ATTACK(InteractionUtils::inputAttack, InteractionUtils::inputAttack, Global.mc.options.keyAttack::isDown),
    USE(InteractionUtils::inputUse, InteractionUtils::inputUse, Global.mc.options.keyUse::isDown),
    FORWARD(InteractionUtils::inputForward, InteractionUtils::inputForward, Global.mc.options.keyUp::isDown),
    BACKWARD(InteractionUtils::inputBackward, InteractionUtils::inputBackward, Global.mc.options.keyDown::isDown),
    STRAFE_LEFT(InteractionUtils::inputStrafeLeft, InteractionUtils::inputStrafeLeft, Global.mc.options.keyLeft::isDown),
    STRAFE_RIGHT(InteractionUtils::inputStrafeRight, InteractionUtils::inputStrafeRight, Global.mc.options.keyRight::isDown),
    JUMP(InteractionUtils::inputJump, InteractionUtils::inputJump, Global.mc.options.keyJump::isDown),
    SPRINT(InteractionUtils::inputToggleSprint, null, Global.mc.options.keySprint::isDown),
    SNEAK(InteractionUtils::inputSneak, null, Global.mc.options.keyShift::isDown),
    LOCK_CURSOR(system.cameraRotator::lockCursor, null, system.cameraRotator::isCursorLocked),
    UNLOCK_CURSOR(system.cameraRotator::unlockCursor, null, () -> !system.cameraRotator.isCursorLocked()),
    LEFT(InteractionUtils::leftClick, null, Global.mc.mouseHandler::isLeftPressed),
    RIGHT(InteractionUtils::rightClick, null, Global.mc.mouseHandler::isRightPressed),
    MIDDLE(InteractionUtils::middleClick, null, Global.mc.mouseHandler::isMiddlePressed),
    INVENTORY(InteractionUtils::inputInventory, null, () -> Global.mc.screen instanceof AbstractContainerScreen<?>),
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
