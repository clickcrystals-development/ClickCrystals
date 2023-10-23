package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void set(Screen screen, CallbackInfo ci) {
        SetScreenEvent event = new SetScreenEvent(screen);
        system.eventBus.pass(event);
        if (event.isCancelled()) {
            ci.cancel();
            setScreen(null);
        }
    }

    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        system.println("Stopping client!");
        system.println("<- disconnecting from discord...");
        ClickCrystals.discordPresence.stop();
        system.println("<- saving data...");
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.saveHuds();
        ClickCrystals.config.saveModules();
        system.println("<- saving config...");
        ClickCrystals.config.save();
    }
}
