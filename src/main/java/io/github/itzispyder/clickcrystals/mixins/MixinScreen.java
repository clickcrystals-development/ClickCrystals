package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.ScreenInitEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoGuiBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen implements Global {

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("HEAD"))
    public void init(MinecraftClient client, int width, int height, CallbackInfo ci) {
        Screen screen = (Screen)(Object)this;
        ScreenInitEvent event = new ScreenInitEvent(screen);
        system.eventBus.pass(event);
    }

    @Inject(method = "applyBlur", at = @At("HEAD"), cancellable = true)
    private void applyBlur(CallbackInfo info) {
        if (Module.get(NoGuiBackground.class).blurToggle.getVal()) {
            info.cancel();
        }
    }

//    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
//    public void OnRenderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        if (Module.get(NoGuiBackground.class).DisableBlackOverlayBackground.getVal() && !(mc.currentScreen instanceof SelectWorldScreen) && !(mc.currentScreen instanceof AddServerScreen)&& !(mc.currentScreen instanceof GameModeSelectionScreen)) {
//            ci.cancel();
//        }
//    }
// I'm trying to make it work ,but it's effecting the other screens so Idk what to do (this "renderBackground" method is responsible for the dark effect when u open some Guis
    @Inject(method = "renderInGameBackground", at = @At("HEAD"), cancellable = true)
    private void renderInGameBackground(CallbackInfo info) {
        if (Module.get(NoGuiBackground.class).DisableBlackOverlayBackground.getVal()) {
            info.cancel();
        }
    }

}
