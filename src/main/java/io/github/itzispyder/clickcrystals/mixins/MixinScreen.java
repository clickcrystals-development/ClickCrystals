package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.ScreenInitEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoGuiBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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

    @Inject(method = "renderInGameBackground", at = @At("HEAD"), cancellable = true)
    private void renderInGameBackground(CallbackInfo info) {
        NoGuiBackground gui = Module.get(NoGuiBackground.class);
        if (gui.isEnabled() && gui.noOverlay.getVal()) {
            info.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V", cancellable = true)
    public void onRenderBackground(DrawContext context, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        NoGuiBackground gui = Module.get(NoGuiBackground.class);
        if (gui.isEnabled() && mc.world != null) {
            ci.cancel();
        }
    }
}


