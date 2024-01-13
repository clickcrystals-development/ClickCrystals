package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoGUiBackground;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {

    @Unique
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(at = @At("HEAD"),
            method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;)V",
            cancellable = true)
    public void onRenderBackground(DrawContext context, CallbackInfo ci) {
        NoGUiBackground noGuiBackground = Module.get(NoGUiBackground.class);

        // Check if the current screen is an instance of HandledScreen
        if (noGuiBackground.isEnabled() && mc.currentScreen instanceof HandledScreen) {
            ci.cancel();
        }
    }
}
