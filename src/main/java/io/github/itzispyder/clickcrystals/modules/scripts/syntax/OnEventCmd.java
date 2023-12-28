package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.scripts.client.ModuleCmd;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class OnEventCmd extends ScriptCommand {

    private static final Map<String, Predicate<BlockState>> defaultedBlockPredicates = new HashMap<>() {{
        this.put("uncharged_respawn_anchor", state -> state.isOf(Blocks.RESPAWN_ANCHOR) && state.get(RespawnAnchorBlock.CHARGES) < 1);
        this.put("charged_respawn_anchor", state -> state.isOf(Blocks.RESPAWN_ANCHOR) && state.get(RespawnAnchorBlock.CHARGES) > 0);
    }};

    public OnEventCmd() {
        super("on");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        EventType type = args.get(0).toEnum(EventType.class, null);

        switch (type) {
            case LEFT_CLICK, RIGHT_CLICK, MIDDLE_CLICK, LEFT_RELEASE, RIGHT_RELEASE, MIDDLE_RELEASE -> passClick(args, type);
            case PLACE_BLOCK, BREAK_BLOCK, INTERACT_BLOCK, PUNCH_BLOCK -> passBlock(args, type);
            case KEY_PRESS, KEY_RELEASE -> passKey(args, type);
            case MOVE_LOOK, MOVE_POS -> passMove(args, type);
            case CHAT_SEND, CHAT_RECEIVE -> passChat(args, type);

            case TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.tickListeners.add(event -> exc(args, 1)));
            case ITEM_USE -> ModuleCmd.runOnCurrentScriptModule(m -> m.itemUseListeners.add(event -> exc(args, 1)));
            case ITEM_CONSUME -> ModuleCmd.runOnCurrentScriptModule(m -> m.itemConsumeListeners.add(event -> exc(args, 1)));
            case MODULE_ENABLE -> ModuleCmd.runOnCurrentScriptModule(m -> m.moduleEnableListeners.add(() -> exc(args, 1)));
            case MODULE_DISABLE -> ModuleCmd.runOnCurrentScriptModule(m -> m.moduleDisableListeners.add(() -> exc(args, 1)));
            case TOTEM_POP -> ModuleCmd.runOnCurrentScriptModule(m -> m.totemPopListeners.add(() -> exc(args, 1)));
            case DAMAGE -> ModuleCmd.runOnCurrentScriptModule(m -> m.damageListeners.add(() -> exc(args, 1)));
            case DEATH -> ModuleCmd.runOnCurrentScriptModule(m -> m.deathListeners.add(() -> exc(args, 1)));
            case GAME_JOIN -> ModuleCmd.runOnCurrentScriptModule(m -> m.gameJoinListeners.add(() -> exc(args, 1)));
            case GAME_LEAVE -> ModuleCmd.runOnCurrentScriptModule(m -> m.gameLeaveListeners.add(() -> exc(args, 1)));
        }
    }

    public void passChat(ScriptArgs args, EventType type) {
        ClickScript dispatcher = args.getExecutorOrDef();
        String msg = args.getQuoteAndRemove(1);
        String rest = args.getAll().toString();

        switch (type) {
            case CHAT_RECEIVE -> ModuleCmd.runOnCurrentScriptModule(m -> m.chatReceiveListeners.add(event -> {
                if (event.getMessage().contains(msg)) {
                    ClickScript.executeDynamic(dispatcher, rest);
                }
            }));
            case CHAT_SEND -> ModuleCmd.runOnCurrentScriptModule(m -> m.chatSendListeners.add(event -> {
                if (event.getMessage().contains(msg)) {
                    ClickScript.executeDynamic(dispatcher, rest);
                }
            }));
        }
    }

    public void passBlock(ScriptArgs args, EventType eventType) {
        switch (eventType) {
            case BREAK_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockBreakListeners.add(event -> exc(args, 1)));
            case PLACE_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockPlaceListeners.add(event -> exc(args, 1)));
            case PUNCH_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockPunchListeners.add((pos, dir) -> exc(args, 1)));
            case INTERACT_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockInteractListeners.add((hit, hand) -> exc(args, 1)));
        }
    }

    public void passClick(ScriptArgs args, EventType eventType) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.clickListeners.add(event -> {
            if (matchMouseClick(eventType, event)) {
                exc(args, 1);
            }
        }));
    }

    public void passKey(ScriptArgs args, EventType eventType) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.keyListeners.add(event -> {
            if (matchKeyPress(eventType, event, args.get(1).toString())) {
                exc(args, 2);
            }
        }));
    }

    public void passMove(ScriptArgs args, EventType eventType) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.moveListeners.add(event -> {
            switch (eventType) {
                case MOVE_LOOK -> {
                    if (event.changesLook()) {
                        exc(args, 1);
                    }
                }
                case MOVE_POS -> {
                    if (event.changesPosition()) {
                        exc(args, 1);
                    }
                }
            }
        }));
    }

    public void exc(ScriptArgs args, int begin) {
        executeWithThen(args, begin);
    }

    private boolean matchKeyPress(EventType type, KeyPressEvent event, String keyName) {
        ClickType action = event.getAction();
        String typed = Keybind.getExtendedKeyName(event.getKeycode(), event.getScancode());

        if (typed == null) {
            return false;
        }

        if (type == EventType.KEY_PRESS && action.isDown()) {
            return keyName.equalsIgnoreCase(typed);
        }
        else if (type == EventType.KEY_RELEASE && action.isRelease()) {
            return keyName.equalsIgnoreCase(typed);
        }
        return false;
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

    public static Predicate<ItemStack> parseItemPredicate(String arg) {
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

    public static Predicate<BlockState> parseBlockPredicate(String arg) {
        if (arg == null || arg.length() <= 1) {
            return block -> false;
        }
        else if (arg.startsWith("#")) {
            return block -> block.getBlock().getTranslationKey().contains(arg.substring(1));
        }
        else if (arg.startsWith(":")) {
            String subArg = arg.substring(1);

            if (defaultedBlockPredicates.containsKey(subArg)) {
                return defaultedBlockPredicates.get(subArg);
            }

            Identifier id = new Identifier("minecraft", subArg);
            return block -> block.getBlock() == Registries.BLOCK.get(id);
        }
        return block -> false;
    }

    public static Predicate<Entity> parseEntityPredicate(String arg) {
        if (arg == null || arg.length() <= 1) {
            return ent -> false;
        }
        else if (arg.startsWith("#")) {
            return ent -> ent.getType().getTranslationKey().contains(arg.substring(1));
        }
        else if (arg.startsWith(":")) {
            Identifier id = new Identifier("minecraft", arg.substring(1));
            return ent -> ent.getType() == Registries.ENTITY_TYPE.get(id);
        }
        return ent -> false;
    }

    public static SoundEvent parseSoundEvent(String arg) {
        if (arg == null || arg.length() <= 1) {
            return null;
        }
        else if (arg.startsWith("#")) {
            String subArg = arg.substring(1);
            Identifier result = null;
            for (Identifier id : Registries.SOUND_EVENT.getIds()) {
                if (id.getPath().contains(subArg)) {
                    result = id;
                    break;
                }
            }
            return result == null ? null : Registries.SOUND_EVENT.get(result);
        }
        else if (arg.startsWith(":")) {
            Identifier id = new Identifier("minecraft", arg.substring(1));
            return Registries.SOUND_EVENT.get(id);
        }
        return null;
    }

    public static StatusEffect parseStatusEffect(String arg) {
        if (arg == null || arg.length() <= 1) {
            return null;
        }
        else if (arg.startsWith("#")) {
            String subArg = arg.substring(1);
            Identifier result = null;
            for (Identifier id : Registries.STATUS_EFFECT.getIds()) {
                if (id.getPath().contains(subArg)) {
                    result = id;
                    break;
                }
            }
            return result == null ? null : Registries.STATUS_EFFECT.get(result);
        }
        else if (arg.startsWith(":")) {
            Identifier id = new Identifier("minecraft", arg.substring(1));
            return Registries.STATUS_EFFECT.get(id);
        }
        return null;
    }

    public static void executeWithThen(ScriptArgs args, int begin) {
        args.executeAll(args.match(begin, "then") ? begin + 1 : begin);
    }

    public enum EventType {
        RIGHT_CLICK,
        LEFT_CLICK,
        MIDDLE_CLICK,
        RIGHT_RELEASE,
        LEFT_RELEASE,
        MIDDLE_RELEASE,
        PLACE_BLOCK,
        BREAK_BLOCK,
        PUNCH_BLOCK,
        INTERACT_BLOCK,
        TICK,
        ITEM_USE,
        ITEM_CONSUME,
        TOTEM_POP,
        MODULE_ENABLE,
        MODULE_DISABLE,
        MOVE_POS,
        MOVE_LOOK,
        KEY_PRESS,
        KEY_RELEASE,
        DAMAGE,
        DEATH,
        GAME_JOIN,
        GAME_LEAVE,
        CHAT_SEND,
        CHAT_RECEIVE
    }
}
