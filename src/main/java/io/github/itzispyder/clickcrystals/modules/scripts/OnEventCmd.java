package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.function.Predicate;

public class OnEventCmd extends ScriptCommand {

    public OnEventCmd() {
        super("on");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        EventType type = args.get(0).enumValue(EventType.class, null);

        switch (type) {
            case LEFT_CLICK, RIGHT_CLICK, MIDDLE_CLICK, LEFT_RELEASE, RIGHT_RELEASE, MIDDLE_RELEASE -> passClick(args, type);
            case PLACE_BLOCK, BREAK_BLOCK, INTERACT_BLOCK, PUNCH_BLOCK -> passBlock(args, type);
            case KEY_PRESS, KEY_RELEASE -> passKey(args, type);
            case MOVE_LOOK, MOVE_POS -> passMove(args, type);

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
            if (matchKeyPress(eventType, event, args)) {
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

    private boolean matchKeyPress(EventType type, KeyPressEvent event, ScriptArgs args) {
        ClickType action = event.getAction();
        String typed = GLFW.glfwGetKeyName(event.getKeycode(), event.getScancode());

        if (typed == null) {
            return false;
        }

        char input = typed.isEmpty() ? ' ' : typed.charAt(0);

        if (type == EventType.KEY_PRESS && action.isDown()) {
            return args.get(1).charValue() == input;
        }
        else if (type == EventType.KEY_RELEASE && action.isRelease()) {
            return args.get(1).charValue() == input;
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
            Identifier id = new Identifier("minecraft", arg.substring(1));
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
        GAME_LEAVE
    }
}
