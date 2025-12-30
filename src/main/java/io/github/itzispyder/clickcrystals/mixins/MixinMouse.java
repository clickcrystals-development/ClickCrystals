package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseScrollEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorMouse;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MixinMouse implements Global, AccessorMouse {

    @Shadow private double cursorDeltaY;
    @Shadow private double cursorDeltaX;
    @Shadow protected abstract void onMouseButton(long window, MouseInput input, int action);
    @Shadow protected abstract void onMouseScroll(long window, double horizontal, double vertical);

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void onButton(long window, MouseInput input, int action, CallbackInfo ci) {
        ClickType click = ClickType.of(action);
        MouseClickEvent event = new MouseClickEvent(input.button(), click);
        system.eventBus.passWithCallbackInfo(ci, event);
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MouseScrollEvent event = new MouseScrollEvent(horizontal, vertical);
        system.eventBus.passWithCallbackInfo(ci, event);
    }

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    public void onCursorPos(CallbackInfo ci) {
        if (system.cameraRotator.isRunningTicket()) {
            cursorDeltaY = cursorDeltaX = 0;
            ci.cancel();
        }

        if (system.cameraRotator.isCursorLocked()) {
            cursorDeltaY = cursorDeltaX = 0;
            ci.cancel();
        }
    }

    @Override
    public void leftClick() {
        onMouseButton(mc.getWindow().getHandle(), new MouseInput(0, 0), 1);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onMouseButton(mc.getWindow().getHandle(), new MouseInput(0, 0), 0);
        }), 50);
    }

    @Override
    public void rightClick() {
        onMouseButton(mc.getWindow().getHandle(), new MouseInput(1, 0), 1);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onMouseButton(mc.getWindow().getHandle(), new MouseInput(1, 0), 0);
        }), 50);
    }

    @Override
    public void middleClick() {
        onMouseButton(mc.getWindow().getHandle(), new MouseInput(2, 0), 1);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onMouseButton(mc.getWindow().getHandle(), new MouseInput(2, 0), 0);
        }), 50);
    }

    @Override
    public void scroll(double amount) {
        onMouseScroll(mc.getWindow().getHandle(), 0, amount);
    }
}
