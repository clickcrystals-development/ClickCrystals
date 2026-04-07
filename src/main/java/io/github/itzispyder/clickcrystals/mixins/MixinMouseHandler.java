package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseScrollEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorMouse;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler implements Global, AccessorMouse {

    @Shadow private double accumulatedDY;
    @Shadow private double accumulatedDX;
    @Shadow protected abstract void onButton(long window, MouseButtonInfo input, int action);
    @Shadow protected abstract void onScroll(long window, double horizontal, double vertical);

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    public void onButton(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci) {
        ClickType click = ClickType.of(action);
        MouseClickEvent event = new MouseClickEvent(rawButtonInfo.button(), click);
        system.eventBus.passWithCallbackInfo(ci, event);
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    public void onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        MouseScrollEvent event = new MouseScrollEvent(xoffset, yoffset);
        system.eventBus.passWithCallbackInfo(ci, event);
    }

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void onCursorPos(CallbackInfo ci) {
        if (system.cameraRotator.isRunningTicket()) {
            accumulatedDY = accumulatedDX = 0;
            ci.cancel();
        }

        if (system.cameraRotator.isCursorLocked()) {
            accumulatedDY = accumulatedDX = 0;
            ci.cancel();
        }
    }

    @Override
    public void leftClick() {
        onButton(mc.getWindow().handle(), new MouseButtonInfo(0, 0), 1);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onButton(mc.getWindow().handle(), new MouseButtonInfo(0, 0), 0);
        }), 50);
    }

    @Override
    public void rightClick() {
        onButton(mc.getWindow().handle(), new MouseButtonInfo(1, 0), 1);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onButton(mc.getWindow().handle(), new MouseButtonInfo(1, 0), 0);
        }), 50);
    }

    @Override
    public void middleClick() {
        onButton(mc.getWindow().handle(), new MouseButtonInfo(2, 0), 1);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onButton(mc.getWindow().handle(), new MouseButtonInfo(2, 0), 0);
        }), 50);
    }

    @Override
    public void scroll(double amount) {
        onScroll(mc.getWindow().handle(), 0, amount);
    }
}
