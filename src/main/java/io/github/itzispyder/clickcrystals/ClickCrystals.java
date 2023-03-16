package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.commands.commands.ClickCrystalToggleCommand;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public final class ClickCrystals implements ModInitializer {

    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ClickCrystalsSystem system = new ClickCrystalsSystem();
    public static final String modId = "clickcrystals", prefix = "[ClickCrystals] ", starter = "§7[§bClick§3Crystals§7] §r";
    public static boolean isEnabled = true;

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        System.out.println(prefix + "Loading ClickCrystals by ImproperIssues");
        this.startTicking();
        system.addCommand(new ClickCrystalToggleCommand());
    }

    /**
     * Start click tick events
     */
    public void startTicking() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientTickEvent.Pre event = new ClientTickEvent.Pre();
            system.eventBus.pass(event);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientTickEvent.End event = new ClientTickEvent.End();
            system.eventBus.pass(event);
        });
    }
}
