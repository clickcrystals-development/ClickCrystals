package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.client.networking.PacketMapper;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.listeners.TickListener;
import net.minecraft.client.sound.SoundInstance;

import java.util.function.Predicate;

public class OnEventCmd extends ScriptCommand implements ThenChainable {

    public OnEventCmd() {
        super("on");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        EventType type = args.get(0).toEnum(EventType.class, null);

        switch (type) {
            case LEFT_CLICK, RIGHT_CLICK, MIDDLE_CLICK, LEFT_RELEASE, RIGHT_RELEASE, MIDDLE_RELEASE, MOUSE_CLICK, MOUSE_RELEASE -> passClick(args, type);
            case PLACE_BLOCK, BREAK_BLOCK, INTERACT_BLOCK, PUNCH_BLOCK -> passBlock(args, type);
            case KEY_PRESS, KEY_RELEASE -> passKey(args, type);
            case MOVE_LOOK, MOVE_POS -> passMove(args, type);
            case CHAT_SEND, CHAT_RECEIVE -> passChat(args, type);
            case PACKET_SEND, PACKET_RECEIVE -> passPacket(args, type);
            case SOUND_PLAY -> passSound(args);

            case MOUSE_WHEEL -> ModuleCmd.runOnCurrentScriptModule(m -> m.mouseWheelListeners.add(() -> exc(args, 1)));
            case MOUSE_WHEEL_UP -> ModuleCmd.runOnCurrentScriptModule(m -> m.mouseWheelUpListeners.add(() -> exc(args, 1)));
            case MOUSE_WHEEL_DOWN -> ModuleCmd.runOnCurrentScriptModule(m -> m.mouseWheelDownListeners.add(() -> exc(args, 1)));

            case PRE_TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.preTickListeners.add(TickListener.fromScript(args, this)));
            case TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.tickListeners.add(TickListener.fromScript(args, this)));
            case POST_TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.postTickListeners.add(TickListener.fromScript(args, this)));

            case ITEM_USE -> ModuleCmd.runOnCurrentScriptModule(m -> m.itemUseListeners.add(event -> exc(args, 1)));
            case ITEM_CONSUME -> ModuleCmd.runOnCurrentScriptModule(m -> m.itemConsumeListeners.add(event -> exc(args, 1)));
            case MODULE_ENABLE -> ModuleCmd.runOnCurrentScriptModule(m -> m.moduleEnableListeners.add(() -> exc(args, 1)));
            case MODULE_DISABLE -> ModuleCmd.runOnCurrentScriptModule(m -> m.moduleDisableListeners.add(() -> exc(args, 1)));
            case TOTEM_POP -> ModuleCmd.runOnCurrentScriptModule(m -> m.totemPopListeners.add(() -> exc(args, 1)));
            case DAMAGE -> ModuleCmd.runOnCurrentScriptModule(m -> m.damageListeners.add(() -> exc(args, 1)));
            case RESPAWN -> ModuleCmd.runOnCurrentScriptModule(m -> m.respawnListeners.add(() -> exc(args, 1)));
            case DEATH -> ModuleCmd.runOnCurrentScriptModule(m -> m.deathListeners.add(() -> exc(args, 1)));
            case GAME_JOIN -> ModuleCmd.runOnCurrentScriptModule(m -> m.gameJoinListeners.add(() -> exc(args, 1)));
            case GAME_LEAVE -> ModuleCmd.runOnCurrentScriptModule(m -> m.gameLeaveListeners.add(() -> exc(args, 1)));
        }
    }

    private void passSound(ScriptArgs args) {
        Predicate<SoundInstance> soundPredicate = ScriptParser.parseSoundInstancePredicate(args.get(1).toString());
        ModuleCmd.runOnCurrentScriptModule(m -> m.soundPlayListeners.add(e -> {
            if (soundPredicate.test(e.getSound()))
                exc(args, 2);
        }));
    }

    private void passPacket(ScriptArgs args, EventType type) {
        String targetPacketName = args.get(1).toString();

        switch (type) {
            case PACKET_RECEIVE -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetReceiveListeners.add(packet -> {
                PacketMapper.Info info = PacketMapper.S2C.get(packet.getClass());
                if (targetPacketName.equals(info.id()))
                    exc(args, 2);
            }));
            case PACKET_SEND -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetSendListeners.add(packet -> {
                PacketMapper.Info info = PacketMapper.C2S.get(packet.getClass());
                if (targetPacketName.equals(info.id()))
                    exc(args, 2);
            }));
        }
    }

    private void passChat(ScriptArgs args, EventType type) {
        ClickScript dispatcher = args.getExecutorOrDef();
        String msg = args.getQuoteAndRemove(1);
        String rest = args.getAll().toString();

        switch (type) {
            case CHAT_RECEIVE -> ModuleCmd.runOnCurrentScriptModule(m -> m.chatReceiveListeners.add(event -> {
                if (event.getMessage().contains(msg) || event.getMessage().matches(msg)) {
                    ClickScript.executeDynamic(dispatcher, rest);
                }
            }));
            case CHAT_SEND -> ModuleCmd.runOnCurrentScriptModule(m -> m.chatSendListeners.add(event -> {
                if (event.getMessage().contains(msg) || event.getMessage().matches(msg)) {
                    ClickScript.executeDynamic(dispatcher, rest);
                }
            }));
        }
    }

    private void passBlock(ScriptArgs args, EventType eventType) {
        switch (eventType) {
            case BREAK_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockBreakListeners.add(event -> exc(args, 1)));
            case PLACE_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockPlaceListeners.add(event -> exc(args, 1)));
            case PUNCH_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockPunchListeners.add((pos, dir) -> exc(args, 1)));
            case INTERACT_BLOCK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockInteractListeners.add((hit, hand) -> exc(args, 1)));
        }
    }

    private void passClick(ScriptArgs args, EventType eventType) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.clickListeners.add(event -> {
            if (eventType == EventType.MOUSE_CLICK && event.getAction().isDown()) {
                int button = args.get(1).toInt();
                if (button == event.getButton())
                    exc(args, 2);
            }
            else if (eventType == EventType.MOUSE_RELEASE && event.getAction().isRelease()) {
                int button = args.get(1).toInt();
                if (button == event.getButton())
                    exc(args, 2);
            }

            if (matchMouseClick(eventType, event)) {
                exc(args, 1);
            }
        }));
    }

    private void passKey(ScriptArgs args, EventType eventType) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.keyListeners.add(event -> {
            if (matchKeyPress(eventType, event, args.get(1).toString())) {
                exc(args, 2);
            }
        }));
    }

    private void passMove(ScriptArgs args, EventType eventType) {
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

    private void exc(ScriptArgs args, int begin) {
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

    public enum EventType {
        RIGHT_CLICK, // @Format on right_click
        LEFT_CLICK, // @Format on left_click
        MIDDLE_CLICK, // @Format on middle_click
        MOUSE_CLICK, // @Format on mouse_click <int>
        RIGHT_RELEASE, // @Format on right_release
        LEFT_RELEASE, // @Format on left_release
        MIDDLE_RELEASE, // @Format on middle_release
        MOUSE_RELEASE, // @Format on mouse_release <int>
        PLACE_BLOCK, // @Format on place_block
        BREAK_BLOCK, // @Format on break_block
        PUNCH_BLOCK, // @Format on punch_block
        INTERACT_BLOCK, // @Format on interact_block
        PRE_TICK, // @Format on pre_tick <int>?
        TICK, // @Format on tick <int>?
        POST_TICK, // @Format on post_tick <int>?
        ITEM_USE, // @Format on item_use
        ITEM_CONSUME, // @Format on item_consume
        TOTEM_POP, // @Format on totem_pop
        MODULE_ENABLE, // @Format on module_enable
        MODULE_DISABLE, // @Format on module_disable
        MOVE_POS, // @Format on move_pos
        MOVE_LOOK, // @Format on move_look
        KEY_PRESS, // @Format on key_press ...
        KEY_RELEASE, // @Format on key_release ...
        DAMAGE, // @Format on damage
        RESPAWN, // @Format on respawn
        DEATH, // @Format on death
        GAME_JOIN, // @Format on game_join
        GAME_LEAVE, // @Format on game_leave
        CHAT_SEND, // @Format on chat_send "..."
        CHAT_RECEIVE, // @Format on chat_receive "..."
        PACKET_SEND, // @Format on packet_send ...
        PACKET_RECEIVE, // @Format on packet_receive ...
        SOUND_PLAY, // @Format on sound_play <identifier>
        MOUSE_WHEEL_UP, // @Format on mouse_wheel_up
        MOUSE_WHEEL_DOWN, // @Format on mouse_wheel_down
        MOUSE_WHEEL // @Format on mouse_wheel
    }
}
