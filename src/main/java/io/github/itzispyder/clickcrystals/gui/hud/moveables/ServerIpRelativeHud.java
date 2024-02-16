package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import net.minecraft.client.MinecraftClient;

public class ServerIpRelativeHud extends TextHud {

    public ServerIpRelativeHud() {
        super("server-ip-hud", 10, 195, 150, 12);
    }

    @Override
    public String getId() {
        return "server_ip_hud";
    }

    @Override
    public String getText() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.isInSingleplayer()) {
            return "In Singleplayer";
        }
        if (client.getCurrentServerEntry() == null) {
            return "Not connected to a server";
        }
        return "Server IP: " + client.getCurrentServerEntry().address;
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Boolean.TRUE.equals(Module.getFrom(InGameHuds.class, m -> m.hudServerIp.getVal()));
    }
}
