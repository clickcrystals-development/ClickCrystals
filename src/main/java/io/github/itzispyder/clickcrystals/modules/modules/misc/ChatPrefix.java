package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;

public class ChatPrefix extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> prefix = scGeneral.add(StringSetting.create()
            .name("chat-prefix")
            .description("Chat message prefix")
            .def("")
            .build()
    );
    public final ModuleSetting<String> suffix = scGeneral.add(StringSetting.create()
            .name("chat-suffix")
            .description("Chat message suffix")
            .def("")
            .build()
    );

    public ChatPrefix() {
        super("chat-prefix", Categories.MISC, "Chat tweaks and additional features.");
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
    private void onSend(ChatSendEvent e) {
        e.setMessage(prefix.getVal() + e.getMessage() + suffix.getVal());
    }
}
