package io.github.itzispyder.clickcrystals.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SelfGlow;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SpectatorSight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.itzispyder.clickcrystals.Global.mc;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void overrideIsInvisibleToPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        SpectatorSight ss = Module.get(SpectatorSight.class);

        if (ss.isEnabled() && ss.canRender((Entity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyReturnValue(method = "getTeamColorValue", at = @At("RETURN"))
    private int modifyGetTeamColorValue(int original) {

        SelfGlow self = Module.get(SelfGlow.class);

        if (self == null || !self.isEnabled() || ((Object) this) != mc.player)
            return original;

        return self.glowColor.getVal().getRGBA();
    }
}
