package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import net.minecraft.client.MinecraftClient;

public class ServerIpRelativeHud extends TextHud {

    public ServerIpRelativeHud() {
        super("server-ip-hud", 10, 190, 150, 12);
    }

    @Override
    public String getText() {
        return "Server IP: " + getServerIp();
    }

    private String getServerIp() {
        if (MinecraftClient.getInstance().getCurrentServerEntry() != null && MinecraftClient.getInstance().getServer() != null) {
            return MinecraftClient.getInstance().getServer().getServerIp();
        }
        return "Not connected to a server";
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Boolean.TRUE.equals(Module.getFrom(InGameHuds.class, m -> m.hudServerIp.getVal()));
    }
}
