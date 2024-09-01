package io.github.itzispyder.clickcrystals.client.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.screens.HomeScreen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!ClickCrystals.config.isDisableModMenuIntegration())
            return parent -> new HomeScreen();
        return null;
    }
}