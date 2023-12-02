package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity implements Global {

    @Shadow @Nullable protected abstract PlayerListEntry getPlayerListEntry();

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<SkinTextures> cir) {
        PlayerListEntry p = getPlayerListEntry();

        if (p == null) {
            return;
        }

        Identifier cape = system.capeManager.getCapeTexture(p.getProfile());

        if (cape != null) {
            SkinTextures s = cir.getReturnValue();
            cir.setReturnValue(new SkinTextures(s.texture(), s.textureUrl(), cape, s.elytraTexture(), s.model(), s.secure()));
        }
    }
}