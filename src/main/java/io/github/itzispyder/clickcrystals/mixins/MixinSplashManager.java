package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class MixinSplashManager implements Global {

    @Shadow private List<String> splashes;
    @Unique private static final String SPLASH_URL = "https://raw.githubusercontent.com/clickcrystals-development/ClickCrystals-Plus-Pack/main/assets/minecraft/texts/splashes.txt";
    @Unique private static List<String> customSplashes = null;

    @Inject(method = "getSplash", at = @At("RETURN"), cancellable = true)
    private void onGet(CallbackInfoReturnable<SplashRenderer> cir) {
        if (splashes.isEmpty() || Math.random() < 0.2)
            return;

        List<String> custom = loadCustomSplashes();
        if (custom.isEmpty())
            return;

        Component text = Component.literal(system.random.getRandomElement(custom));
        SplashRenderer renderer = new SplashRenderer(text);
        cir.setReturnValue(renderer);
    }

    @Unique
    private List<String> loadCustomSplashes() {
        if (customSplashes != null)
            return customSplashes;

        customSplashes = new ArrayList<>();
        try {
            URL url = URI.create(SPLASH_URL).toURL();
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null)
                customSplashes.add(line);

            br.close();
            isr.close();
            is.close();
        }
        catch (Exception ex) {
            system.logger.error("Failed to load custom splash texts -> " + ex.getMessage());
        }
        return customSplashes;
    }
}