package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ChatCommandEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import org.lwjgl.glfw.GLFW;

public class MsgResend extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Keybind> resendKeybind = scGeneral.add(KeybindSetting.create()
            .name("message-resend-keybind")
            .description("Key to resend last message/command.")
            .def(GLFW.GLFW_KEY_UP)
            .onPress(bind -> this.resendMessage())
            .condition((bind, screen) -> screen == null)
            .build()
    );
    private String lastMessage;
    private boolean wasCommand;

    public MsgResend() {
        super("message-resend", Categories.MISC, "Press up arrow key to resend your last message.");
        this.lastMessage = null;
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onChatSend(ChatSendEvent e) {
        final String msg = e.getMessage();

        this.lastMessage = msg;
        this.wasCommand = false;
    }

    @EventHandler
    private void onCommand(ChatCommandEvent e) {
        final String msg = e.getCommandLine();

        this.lastMessage = msg;
        this.wasCommand = true;
    }

    public void resendMessage() {
        if (lastMessage != null && !lastMessage.isEmpty()) {
            if (wasCommand) {
                ChatUtils.sendChatCommand(lastMessage);
            }
            else {
                ChatUtils.sendChatMessage(lastMessage);
            }
        }
    }
}
