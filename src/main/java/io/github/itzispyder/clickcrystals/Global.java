package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.client.system.DiscordPresence;
import io.github.itzispyder.clickcrystals.data.ConfigFile;
import net.minecraft.client.MinecraftClient;

public interface Global {

    MinecraftClient mc = MinecraftClient.getInstance();
    ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    String prefix = ClickCrystals.prefix;
    String version = ClickCrystals.version;
    String starter = ClickCrystals.starter;
    String modId = ClickCrystals.modId;
    ConfigFile config = ClickCrystals.config;
    DiscordPresence rpc = ClickCrystals.discordPresence;

}
