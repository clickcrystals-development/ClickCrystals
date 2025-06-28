package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.networking.PacketMapper;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.modules.scripts.client.ModuleCmd;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class OnEventCmd extends ScriptCommand implements ThenChainable {

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
            case LEFT_CLICK, RIGHT_CLICK, MIDDLE_CLICK, LEFT_RELEASE, RIGHT_RELEASE, MIDDLE_RELEASE, MOUSE_CLICK, MOUSE_RELEASE -> passClick(args, type);
            case PLACE_BLOCK, BREAK_BLOCK, INTERACT_BLOCK, PUNCH_BLOCK -> passBlock(args, type);
            case KEY_PRESS, KEY_RELEASE -> passKey(args, type);
            case MOVE_LOOK, MOVE_POS -> passMove(args, type);
            case CHAT_SEND, CHAT_RECEIVE -> passChat(args, type);
            case PACKET_SEND, PACKET_RECEIVE -> passPacket(args, type);

            case PRE_TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.preTickListeners.add(event -> exc(args, 1)));
            case TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.tickListeners.add(event -> exc(args, 1)));
            case POST_TICK -> ModuleCmd.runOnCurrentScriptModule(m -> m.postTickListeners.add(event -> exc(args, 1)));
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
        RIGHT_CLICK,
        LEFT_CLICK,
        MIDDLE_CLICK,
        MOUSE_CLICK,
        RIGHT_RELEASE,
        LEFT_RELEASE,
        MIDDLE_RELEASE,
        MOUSE_RELEASE,
        PLACE_BLOCK,
        BREAK_BLOCK,
        PUNCH_BLOCK,
        INTERACT_BLOCK,
        PRE_TICK,
        TICK,
        POST_TICK,
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
        RESPAWN,
        DEATH,
        GAME_JOIN,
        GAME_LEAVE,
        CHAT_SEND,
        CHAT_RECEIVE,
        PACKET_SEND,
        PACKET_RECEIVE
    }
}
