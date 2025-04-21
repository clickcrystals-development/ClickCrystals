package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class MixinSplashTextResourceSupplier implements Global {

    @Unique
    private static final String URL = "https://raw.githubusercontent.com/clickcrystals-development/ClickCrystals-Plus-Pack/main/assets/minecraft/texts/splashes.txt";

    @Unique
    private static volatile List<String> splashes = null;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onGet(CallbackInfoReturnable<SplashTextRenderer> cir) {
        cir.setReturnValue(pickSplash(cir.getReturnValue()));
    }

    @Unique
    private SplashTextRenderer pickSplash(SplashTextRenderer original) {
        List<String> list = loadSplashes();
        if (list == null || list.isEmpty()) return original;
        Randomizer rnd = new Randomizer();
        // ~31% chance to keep vanilla
        if (rnd.getRandomInt(1, 100) > 69) {
            return original;
        }
        // safe to index, since we know list has at least one element
        String text = list.get(rnd.getRandomInt(0, list.size() - 1));
        return new SplashTextRenderer(text);
    }

    @Unique
    private List<String> loadSplashes() {
        if (splashes != null) {
            return splashes;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URI(URL).toURL().openStream(), StandardCharsets.UTF_8))) {
            List<String> lines = reader.lines()
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();

            if (!lines.isEmpty()) {
                splashes = List.copyOf(lines);
                system.logger.info("Loaded " + splashes.size() + " custom splash texts");
            }
        } catch (IOException | URISyntaxException e) {
            system.logger.error("Failed to load custom splash texts -> " + e.getMessage());
        }
        return splashes;
    }
}
