package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ChatPrefix extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scFonts = createSettingSection("text-fonts");
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
    public final ModuleSetting<Mode> font = scFonts.add(EnumSetting.create(Mode.class)
            .name("text-font-style")
            .description("Text font styles that only apply to you!")
            .def(Mode.Normal)
            .build()
    );

    public final Map<Character, Character> FONT_SMALL_CAPS = new HashMap<>();
    public final Map<Character, Character> FONT_TINY = new HashMap<>();
    public final Map<Character, Character> FONT_UPSIDE_DOWN = new HashMap<>();
    public final Map<Character, Character> FONT_BUBBLED = new HashMap<>();

    public ChatPrefix() {
        super("chat-prefix", Categories.MISC, "Chat tweaks and additional features.");

        String[] fontNormal =       "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
        String[] fontSmallCaps =    "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘQʀsᴛᴜᴠᴡxʏᴢABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
        String[] fontTiny =         "ᵃᵇᶜᵈᵉᶠᵍʰⁱʲᵏˡᵐⁿᵒᵖᑫʳˢᵗᵘᵛʷˣʸᶻᴬᴮᶜᴰᴱᶠᴳᴴᴵᴶᴷᴸᴹᴺᴼᴾᑫᴿᔆᵀᵁⱽᵂˣʸᶻ".split("");
        String[] fontUpsideDown =   "ɐqɔpǝⅎƃɥᴉɾʞʅɯuodbɹsʇnʌʍxʎz∀ꓭϽᗡƎᖵ⅁HIᒋꓘ⅂ꟽNOԀꝹꓤSꓕՈɅϺX⅄Z".split("");
        String[] fontBubbled =      "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ".split("");

        for (int i = 0; i < fontNormal.length; i++) {
            char n = fontNormal[i].charAt(0);
            FONT_SMALL_CAPS.put(n, fontSmallCaps[i].charAt(0));
            FONT_TINY.put(n, fontTiny[i].charAt(0));
            FONT_UPSIDE_DOWN.put(n, fontUpsideDown[i].charAt(0));
            FONT_BUBBLED.put(n, fontBubbled[i].charAt(0));
        }
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
        String fixed = prefix.getVal() + e.getMessage() + suffix.getVal();
        e.setMessage(applyFont(font.getVal(), fixed));
    }

    public String applyFont(Mode mode, String msg) {
        String res;

        switch (mode) {
            default -> res = msg;
            case SmallCaps -> res = applyFont(FONT_SMALL_CAPS, msg);
            case Tiny -> res = applyFont(FONT_TINY, msg);
            case Bubbled -> res = applyFont(FONT_BUBBLED, msg);
            case UpsideDown -> res = StringUtils.revered(applyFont(FONT_UPSIDE_DOWN, msg));
        }

        return res;
    }

    public String applyFont(Map<Character, Character> FONT, String msg) {
        StringBuilder b = new StringBuilder();
        for (char c : msg.toCharArray()) {
            b.append(FONT.getOrDefault(c, c));
        }
        return b.toString();
    }

    public enum Mode {
        Normal,
        SmallCaps,
        Tiny,
        UpsideDown,
        Bubbled
    }
}
