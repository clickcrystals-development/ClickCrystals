package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui_beta.ClickType;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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

        switch (type) {
            case LEFT_CLICK, RIGHT_CLICK, MIDDLE_CLICK, LEFT_RELEASE, RIGHT_RELEASE, MIDDLE_RELEASE -> passClick(args, type);
            case BLOCK_PLACE, BLOCK_BREAK, BLOCK_INTERACT, BLOCK_PUNCH -> passBlock(args, type);
            case TICK -> passTick(args);
        }
    }

    public void passTick(ScriptArgs args) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.tickListeners.add(event -> {
            ClickScript.executeOneLine(args.getAll(1).stringValue());
        }));
    }

    public void passBlock(ScriptArgs args, EventType eventType) {
        switch (eventType) {
            case BLOCK_BREAK -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockBreakListeners.add(event -> {
                if (parseBlockPredicate(args.get(1).stringValue()).test(event.getState())) {
                    ClickScript.executeOneLine(args.getAll(2).stringValue());
                }
            }));
            case BLOCK_PLACE -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockPlaceListeners.add(event -> {
                if (parseBlockPredicate(args.get(1).stringValue()).test(event.getState())) {
                    ClickScript.executeOneLine(args.getAll(2).stringValue());
                }
            }));
            case BLOCK_PUNCH -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockPunchListeners.add((pos, dir) -> {
                if (PlayerUtils.playerNotNull() && parseBlockPredicate(args.get(1).stringValue()).test(PlayerUtils.getWorld().getBlockState(pos))) {
                    ClickScript.executeOneLine(args.getAll(2).stringValue());
                }
            }));
            case BLOCK_INTERACT -> ModuleCmd.runOnCurrentScriptModule(m -> m.blockInteractListeners.add((hit, hand) -> {
                if (PlayerUtils.playerNotNull() && parseBlockPredicate(args.get(1).stringValue()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()))) {
                    ClickScript.executeOneLine(args.getAll(2).stringValue());
                }
            }));
        }
    }

    public void passClick(ScriptArgs args, EventType eventType) {
        // ex.          on left_click #sword switch :golden_apple
        ModuleCmd.runOnCurrentScriptModule(m -> m.clickListeners.add(event -> {
            if (parseItemPredicate(args.get(1).stringValue()).test(HotbarUtils.getHand())) {
                if (matchMouseClick(eventType, event)) {
                    ClickScript.executeOneLine(args.getAll(2).stringValue());
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

    public enum EventType {
        RIGHT_CLICK,
        LEFT_CLICK,
        MIDDLE_CLICK,
        RIGHT_RELEASE,
        LEFT_RELEASE,
        MIDDLE_RELEASE,
        BLOCK_PLACE,
        BLOCK_BREAK,
        BLOCK_PUNCH,
        BLOCK_INTERACT,
        TICK
    }
}
