package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui_beta.ClickType;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class OnEventCmd extends ScriptCommand {

    public OnEventCmd() {
        super("on");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        EventType type = args.get(0).enumValue(EventType.class, null);
        String toExecute = args.getAll(2).stringValue();

        switch (type) {
            case LEFT_CLICK, RIGHT_CLICK, MIDDLE_CLICK, LEFT_RELEASE, RIGHT_RELEASE, MIDDLE_RELEASE -> passClick(args, type, toExecute);
        }
    }

    public void passClick(ScriptArgs args, EventType eventType, String toExecute) {
        // ex.          on left_click #sword switch :golden_apple
        ModuleCmd.runOnCurrentScriptModule(m -> m.clickListeners.add(event -> {
            if (parsePredicate(args.get(1).stringValue()).test(HotbarUtils.getHand())) {
                if (matchMouseClick(eventType, event)) {
                    ClickScript.executeOneLine(toExecute);
                }
            }
        }));
    }

    private boolean matchMouseClick(EventType type, MouseClickEvent event) {
        ClickType action = event.getAction();
        int b = event.getButton();

        if (action.isDown()) {
            switch (type) {
                case LEFT_CLICK -> {
                    return b == 0;
                }
                case RIGHT_CLICK -> {
                    return b == 1;
                }
                case MIDDLE_CLICK -> {
                    return b == 2;
                }
            }
        }
        else if (action.isRelease()) {
            switch (type) {
                case LEFT_RELEASE -> {
                    return b == 0;
                }
                case RIGHT_RELEASE -> {
                    return b == 1;
                }
                case MIDDLE_RELEASE -> {
                    return b == 2;
                }
            }
        }
        return false;
    }

    public static Predicate<ItemStack> parsePredicate(String arg) {
        if (arg == null || arg.length() <= 1) {
            return item -> false;
        }
        else if (arg.startsWith("#")) {
            return item -> item.getItem().getTranslationKey().contains(arg.substring(1));
        }
        else if (arg.startsWith(":")) {
            Identifier id = new Identifier("minecraft", arg.substring(1));
            return item -> item.getItem() == Registries.ITEM.get(id);
        }
        return item -> false;
    }

    public enum EventType {
        RIGHT_CLICK,
        LEFT_CLICK,
        MIDDLE_CLICK,
        RIGHT_RELEASE,
        LEFT_RELEASE,
        MIDDLE_RELEASE
    }
}
