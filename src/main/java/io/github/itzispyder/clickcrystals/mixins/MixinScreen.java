package io.github.itzispyder.clickcrystals.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.ScreenInitEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoGuiBackground;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen implements Global {

    @Inject(method = "init(II)V", at = @At("HEAD"))
    public void init(int width, int height, CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        ScreenInitEvent event = new ScreenInitEvent(screen);
        system.eventBus.pass(event);
    }

    @Inject(method = "extractTransparentBackground", at = @At("HEAD"), cancellable = true)
    private void renderInGameBackground(GuiGraphicsExtractor context, CallbackInfo info) {
        NoGuiBackground gui = Module.get(NoGuiBackground.class);
        if (gui.isEnabled() && gui.noOverlay.getVal()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "extractBackground", cancellable = true)
    public void onRenderBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        NoGuiBackground gui = Module.get(NoGuiBackground.class);
        if (gui.isEnabled() && mc.level != null) {
            ci.cancel();
        }
    }

    @WrapMethod(method = "defaultHandleGameClickEvent")
    private static void wrapHandleGameClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen, Operation<Void> original) {
        if (event instanceof ClickEvent.Custom custom
                && custom.id().getNamespace().equals(modId)
                && custom.id().getPath().equals("click")) {

            ChatUtils.handleCustomClick();
            return;
        }

        // all other events -> vanilla behavior
        original.call(event, minecraft, activeScreen);
    }
}

