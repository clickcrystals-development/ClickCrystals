package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.world.ClientWorld;

// @Format disconnect
public class DisconnectCmd extends ScriptCommand implements ThenChainable {

    public DisconnectCmd() {
        super("disconnect");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        GameMenuScreen.disconnect(mc, ClientWorld.QUITTING_MULTIPLAYER_TEXT);
        executeWithThen(args);
    }
}
