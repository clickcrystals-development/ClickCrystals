package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.RenderInventorySlotEvent;
import io.github.itzispyder.clickcrystals.interfaces.HandledScreenAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen implements Global, HandledScreenAccessor {

    @Shadow protected int x;
    @Shadow protected int y;

    @Shadow protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Inject(method = "drawSlot", at = @At("HEAD"))
    public void drawItemInSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        system.eventBus.pass(new RenderInventorySlotEvent(context, slot.getStack(), slot.x + x, slot.y + y, slot.x, slot.y));
    }

    @Override
    public boolean isHovered(Slot slot, double mouseX, double mouseY) {
        return this.isPointOverSlot(slot, mouseX, mouseY);
    }
}
