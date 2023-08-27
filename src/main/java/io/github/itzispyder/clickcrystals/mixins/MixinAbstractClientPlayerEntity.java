package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity implements Global {

    @Shadow @Nullable protected abstract PlayerListEntry getPlayerListEntry();

    @Inject(method = "getCapeTexture", at = @At("RETURN"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        PlayerListEntry entry = getPlayerListEntry();

        if (entry == null) {
            cir.setReturnValue(cir.getReturnValue());
            return;
        }

        UUID id = entry.getProfile().getId();
        ClickCrystalsInfo.ClickCrystalsUser userAsOwner = ClickCrystals.info.getOwner(id);

        if (userAsOwner != null) {
            cir.setReturnValue(GuiTextures.CLICKCRYSTALS_CAPE_DEV);
            return;
        }

        ClickCrystalsInfo.ClickCrystalsUser userAsStaff = ClickCrystals.info.getStaff(id);

        if (userAsStaff != null) {
            cir.setReturnValue(GuiTextures.CLICKCRYSTALS_CAPE);
            return;
        }

        cir.setReturnValue(cir.getReturnValue());
    }
}
