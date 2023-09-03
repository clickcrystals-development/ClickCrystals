package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseScrollEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MixinMouse implements Global {

    @Shadow private double cursorDeltaY;
    @Shadow private double cursorDeltaX;

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        ClickType click = ClickType.of(action);
        MouseClickEvent event = new MouseClickEvent(button, click);
        system.eventBus.passWithCallbackInfo(ci, event);
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseButton(long window, double horizontal, double vertical, CallbackInfo ci) {
        MouseScrollEvent event = new MouseScrollEvent(horizontal, vertical);
        system.eventBus.passWithCallbackInfo(ci, event);
    }

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    public void onCursorPos(CallbackInfo ci) {
        if (CameraRotator.isCursorLocked()) {
            cursorDeltaY = cursorDeltaX = 0;
            ci.cancel();
        }
    }
}
