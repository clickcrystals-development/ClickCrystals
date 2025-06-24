package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.DrawSlotEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class MixinHandledScreens implements Global {
    // this event is only when the gui is open, this won't work in the hotbar alone (this is intended).
    @Inject(method = "drawSlot", at = @At("HEAD"))
    private static void highlightSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new DrawSlotEvent(context, slot));
    }
}