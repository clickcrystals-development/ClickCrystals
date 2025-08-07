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
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import org.lwjgl.glfw.GLFW;

public class MsgResend extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> resendCommandOnly = scGeneral.add(createBoolSetting()
            .name("resend-only-scripts")
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

    private final Voidable<String> lastMessage, lastCommand;
    private boolean wasCommand;

    public MsgResend() {
        super("message-resend", Categories.MISC, "Press up arrow key to resend your last message or command.");
        this.lastMessage = Voidable.empty();
        this.lastCommand = Voidable.empty();
    }

    @EventHandler
    private void onChatSend(ChatSendEvent e) {
        this.lastMessage.set(e.getMessage());
        this.wasCommand = false;
    }

    @EventHandler
    private void onCommand(ChatCommandEvent e) {
        this.lastCommand.set(e.getCommandLine());
        this.wasCommand = true;
    }

    public void resendMessage() {
        if (!wasCommand && resendCommandOnly.getVal()) {
            lastCommand.accept(ChatUtils::sendChatCommand);
            return;
        }

        if (wasCommand || resendCommandOnly.getVal())
            lastCommand.accept(ChatUtils::sendChatCommand);
        else
            lastMessage.accept(ChatUtils::sendChatMessage);
    }
}