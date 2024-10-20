package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.ChatCommandEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class MsgResend extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> resendCommandOnly = scGeneral.add(createBoolSetting()
            .name("resend-only-commands")
            .description("Resend the last command that you have typed.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Keybind> resendKeybind = scGeneral.add(KeybindSetting.create()
            .name("message-resend-keybind")
            .description("Key to resend last message/command.")
            .def(GLFW.GLFW_KEY_UP)
            .onPress(bind -> this.resendMessage())
            .condition((bind, screen) -> screen == null)
            .build()
    );

    private String lastMessage;
    private String lastCommand;
    private final Map<String, String> commandCacheSaver = new HashMap<>();

    public MsgResend() {
        super("message-resend", Categories.MISC, "Press up arrow key to resend your last message or command.");
        this.lastMessage = null;
        this.lastCommand = null;
    }

    @EventHandler
    private void onChatSend(ChatSendEvent e) {
        this.lastMessage = e.getMessage();
    }

    @EventHandler
    private void onCommand(ChatCommandEvent e) {
        this.lastCommand = e.getCommandLine();
        commandCacheSaver.put("lastCommand", lastCommand);
    }

    public void resendMessage() {
        if (resendCommandOnly.getVal()) {
            String savedCommand = commandCacheSaver.get("lastCommand");
            if (savedCommand != null && !savedCommand.trim().isEmpty()) {
                ChatUtils.sendChatCommand(savedCommand);
            }
        } else if (lastMessage != null && !lastMessage.trim().isEmpty()) {
            ChatUtils.sendChatMessage(lastMessage);
        }
    }
}