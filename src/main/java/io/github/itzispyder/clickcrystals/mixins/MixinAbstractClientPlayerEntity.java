package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayerEntity implements Global {

    @Shadow @Nullable protected abstract PlayerInfo getPlayerInfo();

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerInfo p = getPlayerInfo();

        if (p == null) {
            return;
        }

        Identifier cape = system.capeManager.getCapeTexture(p.getProfile());

        if (cape != null) {
            PlayerSkin s = cir.getReturnValue();
            cir.setReturnValue(new PlayerSkin(s.body(), s.cape(), s.elytra(), s.model(), s.secure()));
        }
    }
}