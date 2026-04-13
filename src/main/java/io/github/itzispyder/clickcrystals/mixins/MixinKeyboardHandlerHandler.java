package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorKeyboardHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandlerHandler implements Global, AccessorKeyboardHandler {

    @Shadow protected abstract void keyPress(long window, int action, KeyEvent input);

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    public void onKeyPress(long window, int action, KeyEvent input, CallbackInfo ci) {
        this.handleKeyPress(input.key(), input.scancode(), action, ci);
    }

    private void handleKeyPress(int key, int scancode, int action, CallbackInfo ci) {
        ClickType a = ClickType.of(action);
        KeyPressEvent e = new KeyPressEvent(key, scancode, a);
        system.eventBus.passWithCallbackInfo(ci, e);
    }

    @Override
    public void clickCrystals$pressKey(int key, int scan) {
        keyPress(mc.getWindow().handle(), 1, new KeyEvent(key, scan, 0));
        system.scheduler.runDelayedTask(() -> mc.execute(() -> {
            keyPress(mc.getWindow().handle(), 0, new KeyEvent(key, scan, 0));
        }), 50);
    }
}