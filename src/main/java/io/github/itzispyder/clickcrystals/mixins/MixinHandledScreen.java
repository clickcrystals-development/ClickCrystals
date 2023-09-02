package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.RenderInventorySlotEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen implements Global {

    @Shadow protected int x;
    @Shadow protected int y;

    @Inject(method = "drawSlot", at = @At("HEAD"))
    public void drawItemInSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        RenderInventorySlotEvent event = new RenderInventorySlotEvent(slot.getStack(), slot.x + x, slot.y + y);
        system.eventBus.pass(event);
    }
}
