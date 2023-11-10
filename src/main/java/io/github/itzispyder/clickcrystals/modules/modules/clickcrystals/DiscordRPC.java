package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.system.DiscordPresence;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;

public class DiscordRPC extends Module {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> rpcDetails = scGeneral.add(StringSetting.create()
            .name("Presence Details")
            .description("Say something in your discord presence! §e(May take up to minutes to update)")
            .def(DiscordPresence.DETAIL)
            .onSettingChange(setting -> ClickCrystals.discordPresence.setDetail(setting.getVal()))
            .build()
    );

    public DiscordRPC() {
        super("discord-rpc", Categories.CLIENT, "Show off your new mod to your friends in Discord!");
    }

    @Override
    protected void onEnable() {
        ClickCrystals.discordPresence.start();
        ClickCrystals.discordPresence.setDetail(rpcDetails.getVal());
    }

    @Override
    protected void onDisable() {
        ClickCrystals.discordPresence.stop();
    }

    public static void syncRPC() {
        ModuleSetting<String> s = get(DiscordRPC.class).rpcDetails;
        s.getChangeAction().onChange(s);
    }
}
