package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.commands.commands.ClickCrystalToggleCommand;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEvent;
import io.github.itzispyder.clickcrystals.modules.modules.ClickCrystal;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public final class ClickCrystals implements ModInitializer {

    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ClickCrystalsSystem system = new ClickCrystalsSystem();

    @SuppressWarnings("unused")
    public static final String modId = "clickcrystals", prefix = "[ClickCrystals] ", starter = "§7[§bClick§3Crystals§7] §r";

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        // Mod initialization
        System.out.println(prefix + "Loading ClickCrystals by ImproperIssues");
        this.startTicking();

        // Commands
        system.addCommand(new ClickCrystalToggleCommand());

        // Module
        system.addModule(new ClickCrystal());
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
