package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.interfaces.KeyboardAccessor;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard implements Global, KeyboardAccessor {

    @Shadow public abstract void onKey(long window, int key, int scancode, int action, int modifiers);

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        this.handleKeyPress(key, scancode, action, ci);
    }

    private void handleKeyPress(int key, int scancode, int action, CallbackInfo ci) {
        ClickType a = ClickType.of(action);
        KeyPressEvent e = new KeyPressEvent(key, scancode, a);
        system.eventBus.passWithCallbackInfo(ci, e);
    }

    @Override
    public void pressKey(int key, int scan) {
        onKey(mc.getWindow().getHandle(), key, scan, 1, 0);
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            onKey(mc.getWindow().getHandle(), key, scan, 0, 0);
        }), 50);
    }
}
