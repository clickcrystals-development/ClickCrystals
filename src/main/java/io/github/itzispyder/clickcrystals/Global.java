package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.client.system.Version;
import net.minecraft.client.MinecraftClient;

public interface Global {

    MinecraftClient mc = MinecraftClient.getInstance();
    ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();

    String prefix = "[Vortex] ";
    String starter = "§7[§bVortex§7] §r";
    String modId = "vortex";
    Version version = Version.ofString(Version.getModVersion());

}
