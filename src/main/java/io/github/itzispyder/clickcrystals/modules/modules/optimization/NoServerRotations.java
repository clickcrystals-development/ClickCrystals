package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.client.client.FakeRotate;
import io.github.itzispyder.clickcrystals.client.client.NoRotationsPacket;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import net.minecraft.entity.player.PlayerEntity;

public class NoServerRotations extends ListenerModule {

    private boolean enabled = false;

    public NoServerRotations() {
        super("no-server-rotations", Categories.LAG, "DEBUG: Sends received EntityStatusPackets in chat");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        enabled = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void tick(PlayerEntity player) {
        if (enabled) {
            NoRotationsPacket.tick(player);
            FakeRotate.tick(player);
        }
    }
}
