package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.commands.Command;
import net.minecraft.command.CommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class ClientInfoCommand extends Command {

    public ClientInfoCommand() {
        super("info", "Display ClickCrystals and Minecraft version.", ",info");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
            String clickCrystalsVersion = Global.version.toString();

            String minecraftVersion = getMinecraftVersionNumber();

            String message = String.format("§bClick§3Crystals§r version: §b%s,§r Minecraft version: §9%s", clickCrystalsVersion, minecraftVersion);
            sendChatMessage(message);

            return SINGLE_SUCCESS;
        });
    }

    private void sendChatMessage(String message) {

        info(message);
    }

    private String getMinecraftVersionNumber() {
        String versionNumber = "Unknown";

        FabricLoader fabricLoader = FabricLoader.getInstance();

        ModContainer modContainer = fabricLoader.getModContainer("minecraft").orElse(null);

        if (modContainer != null) {
            versionNumber = modContainer.getMetadata().getVersion().getFriendlyString();
        }

        return versionNumber;
    }
}
