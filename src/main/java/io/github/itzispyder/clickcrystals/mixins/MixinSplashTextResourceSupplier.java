package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashTextResourceSupplier implements Global {

    @Shadow @Final private List<String> splashTexts;

    @Unique private static final String SPLASH_URL = "https://raw.githubusercontent.com/clickcrystals-development/ClickCrystals-Plus-Pack/main/assets/minecraft/texts/splashes.txt";

    @Unique private static List<String> customSplashes = null;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onGet(CallbackInfoReturnable<SplashTextRenderer> cir) {
        List<String> custom = loadCustomSplashes();
        if (custom == null || custom.isEmpty() || splashTexts.isEmpty()) {
            return;
        }

        Randomizer rnd = new Randomizer();
        int roll = rnd.getRandomInt(1, 100);
        String text;

        if (roll <= 80) {
            // 80% vanilla
            text = splashTexts.get(rnd.getRandomInt(0, splashTexts.size() - 1));
        } else {
            // 20% clickcrystals
            text = custom.get(rnd.getRandomInt(0, custom.size() - 1));
        }
        cir.setReturnValue(new SplashTextRenderer(text));
    }

    @Unique
    private List<String> loadCustomSplashes() {
        if (customSplashes != null) return customSplashes;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(URI.create(SPLASH_URL).toURL().openStream(), StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines()
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();

            if (!lines.isEmpty()) {
                customSplashes = lines;
                system.logger.info("Loaded " + customSplashes.size() + " custom splash texts.");
            }
        } catch (IOException e) {
            system.logger.error("Failed to load custom splash texts -> " + e.getMessage());
        }

        return customSplashes;
    }
}
