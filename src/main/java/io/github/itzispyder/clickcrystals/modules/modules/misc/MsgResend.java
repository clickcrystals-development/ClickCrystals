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

public class MsgResend extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> resendCommandOnly = scGeneral.add(createBoolSetting()
            .name("resend-only-commands")
            .description("Make the module to resend only the commands that you written.")
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
    private boolean wasCommand;

    public MsgResend() {
        super("message-resend", Categories.MISC, "Press up arrow key to resend your last message");
        this.lastMessage = null;
    }

    @EventHandler
    private void onChatSend(ChatSendEvent e) {
        this.lastMessage = e.getMessage();
        this.wasCommand = lastMessage.startsWith("/");
    }

    @EventHandler
    private void onCommand(ChatCommandEvent e) {
        this.lastMessage = e.getCommandLine();
        this.wasCommand = true;
    }

    public void resendMessage() {
        if (lastMessage == null || lastMessage.trim().isEmpty())
            return;

        if (resendCommandOnly.getVal() && wasCommand)
            ChatUtils.sendChatCommand(lastMessage);
        else if (!resendCommandOnly.getVal() && !wasCommand)
            ChatUtils.sendChatMessage(lastMessage);
    }
}
