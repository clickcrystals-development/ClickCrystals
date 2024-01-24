package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.commands.Command;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.command.CommandSource;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("version", "Display ClickCrystals and Minecraft version.", ",version");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
            String ccVer = Global.version.toString();
            String mcVer = getMinecraftVersionNumber();

            Notification.create()
                    .ccIcon()
                    .id("version-info")
                    .stayTime(3000)
                    .title("Running Version")
                    .text("&7ClickCrystals: &f%s    &7Minecraft: &f%s".formatted(ccVer, mcVer))
                    .build()
                    .sendToClient();

            return SINGLE_SUCCESS;
        });
    }

    private String getMinecraftVersionNumber() {
        FabricLoader loader = FabricLoader.getInstance();
        ModContainer container = loader.getModContainer("minecraft").orElse(null);

        if (container != null) {
            return container.getMetadata().getVersion().getFriendlyString();
        }
        return "Unknown";
    }
}
