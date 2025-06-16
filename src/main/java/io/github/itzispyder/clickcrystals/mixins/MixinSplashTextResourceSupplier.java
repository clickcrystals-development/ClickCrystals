package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashTextResourceSupplier implements Global {

    @Shadow @Final private List<String> splashTexts;
    @Unique private static final String SPLASH_URL = "https://raw.githubusercontent.com/clickcrystals-development/ClickCrystals-Plus-Pack/main/assets/minecraft/texts/splashes.txt";
    @Unique private static List<String> customSplashes = null;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onGet(CallbackInfoReturnable<SplashTextRenderer> cir) {
        List<String> custom = loadCustomSplashes();
        if (custom == null || custom.isEmpty() || splashTexts.isEmpty())
            return;

        List<String> pool = Math.random() < 0.8 ? splashTexts : custom;
        String text = system.random.getRandomElement(pool);
        cir.setReturnValue(new SplashTextRenderer(text));
    }

    @Unique
    private List<String> loadCustomSplashes() {
        if (customSplashes != null)
            return customSplashes;

        try (InputStream is = URI.create(SPLASH_URL).toURL().openStream()) {
            String texts = new String(is.readAllBytes());
            String[] lines = texts.split("\\s*\n\\s*");

            if (lines.length == 0)
                return customSplashes;
            customSplashes = ArrayUtils.toList(lines);
            system.logger.info("Loaded " + customSplashes.size() + " custom splash texts.");
        }
        catch (Exception ex) {
            system.logger.error("Failed to load custom splash texts -> " + ex.getMessage());
        }

        return customSplashes;
    }
}
