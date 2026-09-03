package io.github.itzispyder.clickcrystals.gui.screens.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.networking.PacketMapper;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.components.Conditionals;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ConfigCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.DefineCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.logic.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Dimensions;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameType;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.*;

public class ClickScriptAutocomplete implements Global {

    private static final int MAX_SUGGESTIONS = 8;
    private static final int ROW_H = 10;
    private static final int PADDING = 4;
    private static final int POPUP_W = 120;
    private static final int COLOR_BG = 0xF0141420;
    private static final int COLOR_BORDER = 0xFF00B7FF;
    private static final int COLOR_SELECTED = 0x4000B7FF;
    private static final int COLOR_TEXT = 0xFFAAAAAA;
    private static final int COLOR_HIGHLIGHT = 0xFFFFFFFF;

    private static final Set<String> COMMAND_SET;
    private static final List<String>
            COMMANDS,
            EVENT_TYPES,
            CONDITIONALS,
            MODULE_ACTIONS,
            CONFIG_TYPES,
            DEFINE_TYPES,
            INPUT_TYPES,
            DIMENSIONS,
            AS_TYPES,
            INTERACT_TYPES,
            CAMERA_TYPES,
            GAMEMODE_TYPES,
            DIRECTION_TYPES,
            HAND_TYPES,
            PACKET_TYPES,
            PACKET_C2S,
            PACKET_S2C,
            CANCELABLE_INPUT_TYPES,
            KEY_NAMES,
            ITEM_IDS,
            HAND_OR_ITEM_TYPES,
            SWITCH_TYPES;

    static {
        COMMANDS = sorted(Arrays.asList(ClickScript.collectNames()));
        COMMAND_SET = new HashSet<>(COMMANDS);
        EVENT_TYPES = enumValues(OnEventCmd.EventType.class);
        CONDITIONALS = sorted(new ArrayList<>(Conditionals.registeredNames()));
        MODULE_ACTIONS = enumValues(ModuleCmd.Action.class);
        CONFIG_TYPES = enumValues(ConfigCmd.Type.class);
        DEFINE_TYPES = enumValues(DefineCmd.Type.class);
        INPUT_TYPES = enumValues(InputType.class);
        DIMENSIONS = enumValues(Dimensions.class);
        AS_TYPES = enumValues(TargetType.ANY_ENTITY, TargetType.CLIENT, TargetType.NEAREST_ENTITY, TargetType.TARGET_ENTITY);
        INTERACT_TYPES = enumValues(TargetType.ANY_BLOCK, TargetType.ANY_ENTITY, TargetType.NEAREST_BLOCK, TargetType.NEAREST_ENTITY, TargetType.POSITION);
        CAMERA_TYPES = enumValues(TargetType.ANY_BLOCK, TargetType.ANY_ENTITY, TargetType.NEAREST_BLOCK, TargetType.NEAREST_ENTITY, TargetType.POLAR, TargetType.POSITION);
        GAMEMODE_TYPES = enumValues(GameType.class);
        DIRECTION_TYPES = enumValues(Direction.class);
        HAND_TYPES = List.of("mainhand", "offhand");
        PACKET_TYPES = List.of("c2s", "s2c");
        PACKET_C2S = sorted(PacketMapper.C2S.values().stream().map(PacketMapper.Info::id).toList());
        PACKET_S2C = sorted(PacketMapper.S2C.values().stream().map(PacketMapper.Info::id).toList());
        CANCELABLE_INPUT_TYPES = sorted(new ArrayList<>(INPUT_TYPES) {{
            add("cancel");
        }});
        KEY_NAMES = sorted(keyNames());
        ITEM_IDS = sorted(BuiltInRegistries.ITEM.keySet().stream().map(id -> ":" + id.getPath()).toList());
        HAND_OR_ITEM_TYPES = sorted(new ArrayList<>(ITEM_IDS) {{
            addAll(HAND_TYPES);
        }});
        SWITCH_TYPES = sorted(new ArrayList<>(ITEM_IDS) {{
            add("back");
        }});
    }

    // Key names accepted by "on key_press"/"on key_release": the named keys plus the
    // single-character glfw key names (letters and digits) reported at runtime.
    private static List<String> keyNames() {
        List<String> names = new ArrayList<>(Keybind.EXTENDED_NAMES.values());
        for (char c = 'a'; c <= 'z'; c++) names.add(String.valueOf(c));
        for (char c = '0'; c <= '9'; c++) names.add(String.valueOf(c));
        return names;
    }

    private final List<String> suggestions = new ArrayList<>();
    private int selectedIndex = 0;
    private boolean visible = false;

    private static <E extends Enum<E>> List<String> enumValues(Class<E> cls) {
        return sorted(Arrays.stream(cls.getEnumConstants()).map(e -> e.name().toLowerCase()).toList());
    }

    @SafeVarargs
    private static <E extends Enum<E>> List<String> enumValues(E... values) {
        return sorted(Arrays.stream(values).map(e -> e.name().toLowerCase()).toList());
    }

    private static List<String> sorted(List<String> list) {
        return list.stream().sorted().toList();
    }

    // Matches a suggestion against the typed prefix. Identifier suggestions (":item") are also
    // matched by their bare name, so the leading ":" is optional while typing.
    private static boolean matches(String keyword, String prefix) {
        if (keyword.equals(prefix)) return false;
        if (keyword.startsWith(prefix)) return true;
        return isSigil(keyword.charAt(0)) && !isSigil(prefix.charAt(0)) && keyword.regionMatches(1, prefix, 0, prefix.length());
    }

    private static boolean isSigil(char c) {
        return c == ':' || c == '#';
    }

    public void update(String line, int cursorCol) {
        suggestions.clear();
        visible = false;

        if (line.startsWith("//")) return;

        String beforeCursor = cursorCol <= line.length() ? line.substring(0, cursorCol) : line;
        String[] tokens = beforeCursor.stripLeading().split(" +", -1);
        int tokenIdx = tokens.length - 1;
        String prefix = tokenIdx >= 0 ? tokens[tokenIdx] : "";

        // Skip blank lines; allow empty prefix on subsequent tokens (e.g. "on " -> show event types).
        if (prefix.isEmpty() && tokenIdx == 0) return;

        // Never suggest when the user is already typing a number.
        if (!prefix.isEmpty() && (prefix.charAt(0) == '-' || Character.isDigit(prefix.charAt(0)))) return;

        List<String> pool = resolvePool(tokens, tokenIdx);
        for (String kw : pool) {
            if (matches(kw, prefix)) {
                suggestions.add(kw);
                if (suggestions.size() >= MAX_SUGGESTIONS) break;
            }
        }

        visible = !suggestions.isEmpty();
        selectedIndex = 0;
    }

    private List<String> resolvePool(String[] tokens, int tokenIdx) {
        if (tokenIdx == 0) return COMMANDS;
        String cmd = tokens[0].toLowerCase();
        if (cmd.equals("on")) return resolveOnPool(tokens, tokenIdx);
        if (cmd.equals("if") || cmd.equals("if_not")) return resolveIfPool(tokens, tokenIdx);
        if (cmd.equals("while") || cmd.equals("while_not")) return resolveWhilePool(tokens, tokenIdx);
        if ((cmd.equals("cancel_packet") || cmd.equals("uncancel_packet")) && tokenIdx == 2)
            return switch (tokens[1].toLowerCase()) {
                case "c2s" -> PACKET_C2S;
                case "s2c" -> PACKET_S2C;
                default -> List.of();
            };
        return firstArgPool(cmd, tokenIdx);
    }

    // "on": on <event_type> [event_args...] <inline_command> [cmd_args...]
    // Each event type consumes a fixed number of args before the inline command.
    private List<String> resolveOnPool(String[] tokens, int tokenIdx) {
        if (tokenIdx == 1) return EVENT_TYPES;
        String event = tokens[1].toLowerCase();

        // variable-arity events (key names, packet names): scan for inline command
        boolean variable = switch (event) {
            case "key_press", "key_release", "packet_receive", "packet_send" -> true;
            default -> false;
        };
        if (variable) {
            // key events take a key name before the inline command
            if ((event.equals("key_press") || event.equals("key_release")) && tokenIdx == 2)
                return KEY_NAMES;
            for (int i = 2; i < tokenIdx; i++) {
                if (COMMAND_SET.contains(tokens[i].toLowerCase()))
                    return firstArgPool(tokens[i].toLowerCase(), tokenIdx - i);
            }
            return COMMANDS;
        }

        // fixed-arity: determine how many tokens the event itself consumes
        int arity = switch (event) {
            case "chat_receive", "chat_send", "sound_play", "mouse_click", "mouse_release" -> 1;
            // optional <int> prefix for tick events — check if token 2 is a number
            case "tick", "pre_tick", "post_tick" -> tokens.length > 2 && isNumber(tokens[2]) ? 1 : 0;
            default -> 0;
        };

        int inlineCmdPos = 2 + arity;
        if (tokenIdx < inlineCmdPos) return List.of();
        if (tokenIdx == inlineCmdPos) return COMMANDS;
        return firstArgPool(tokens[inlineCmdPos].toLowerCase(), tokenIdx - inlineCmdPos);
    }

    // "if"/"if_not": if <conditional> [cond_args...] <inline_command> [cmd_args...]
    // Uses the registered arity to precisely locate the inline command position.
    private List<String> resolveIfPool(String[] tokens, int tokenIdx) {
        if (tokenIdx == 1) return CONDITIONALS;

        String condName = tokens[1].toLowerCase();
        int condArity = Conditionals.getArity(condName);

        if (condArity >= 0) {
            // Schema-driven: we know exactly how many tokens the conditional consumes.
            int inlineCmdPos = 2 + condArity;
            if (tokenIdx < inlineCmdPos) return condArgPool(condName, tokenIdx - 2);
            if (tokenIdx == inlineCmdPos) return COMMANDS;   // cursor is on the inline command token
            return firstArgPool(tokens[inlineCmdPos].toLowerCase(), tokenIdx - inlineCmdPos);
        }

        // Unknown conditional: fall back to scanning for the first known command.
        for (int i = 2; i < tokenIdx; i++) {
            if (COMMAND_SET.contains(tokens[i].toLowerCase())) {
                return firstArgPool(tokens[i].toLowerCase(), tokenIdx - i);
            }
        }
        return COMMANDS;
    }

    // "while"/"while_not": while <num>? <conditional> [cond_args...] {}
    private List<String> resolveWhilePool(String[] tokens, int tokenIdx) {
        if (tokenIdx == 1) return CONDITIONALS;
        boolean hasNum = isNumber(tokens[1]);
        if (tokenIdx == 2 && hasNum) return CONDITIONALS;
        int condPos = hasNum ? 2 : 1;
        return condArgPool(tokens[condPos].toLowerCase(), tokenIdx - condPos - 1);
    }

    private static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Returns the suggestion pool for a conditional's argument at the given index.
    private static List<String> condArgPool(String cond, int argIdx) {
        if (argIdx < 0) return List.of();
        return switch (cond) {
            case "input_active" -> argIdx == 0 ? INPUT_TYPES : List.of();
            case "gamemode" -> argIdx == 0 ? GAMEMODE_TYPES : List.of();
            case "facing", "target_block_face" -> argIdx == 0 ? DIRECTION_TYPES : List.of();
            case "reference_entity" -> argIdx == 0 ? AS_TYPES : List.of();
            case "dimension" -> argIdx == 0 ? DIMENSIONS : List.of();
            case "item_count", "item_durability", "item_cooldown" ->
                    argIdx == 0 ? HAND_OR_ITEM_TYPES : List.of();
            case "holding", "off_holding", "inventory_has", "inventory_count",
                 "equipment_has", "hotbar_has", "hotbar_count", "cursor_item" ->
                    argIdx == 0 ? ITEM_IDS : List.of();
            default -> List.of();
        };
    }

    // Returns the suggestion pool for the first argument of a command, or nothing for any other position.
    // Also handles inline commands (e.g. the "as" in "if blocking as client").
    private static List<String> firstArgPool(String cmd, int idx) {
        if (idx != 1) return List.of();
        return switch (cmd) {
            case "module" -> MODULE_ACTIONS;
            case "config" -> CONFIG_TYPES;
            case "define", "def" -> DEFINE_TYPES;
            case "input" -> INPUT_TYPES;
            case "hold_input", "toggle_input" -> CANCELABLE_INPUT_TYPES;
            case "interact", "damage" -> INTERACT_TYPES;
            case "cancel_packet", "uncancel_packet" -> PACKET_TYPES;
            case "switch" -> SWITCH_TYPES;
            case "gui_quickmove", "gui_swap", "gui_switch", "gui_drop", "craft" -> ITEM_IDS;
            case "drop" -> List.of("all");
            case "dimension" -> DIMENSIONS;
            case "as" -> AS_TYPES;
            case "snap_to", "turn_to" -> CAMERA_TYPES;
            case "while", "while_not" -> CONDITIONALS;
            default -> List.of();
        };
    }

    public boolean onKey(int key) {
        if (!visible) return false;
        return switch (key) {
            case InputConstants.KEY_UP -> {
                selectedIndex = (selectedIndex - 1 + suggestions.size()) % suggestions.size();
                yield true;
            }
            case InputConstants.KEY_DOWN -> {
                selectedIndex = (selectedIndex + 1) % suggestions.size();
                yield true;
            }
            case InputConstants.KEY_ESCAPE -> {
                visible = false;
                yield true;
            }
            default -> false;
        };
    }

    public boolean isInsertKey(int key) {
        return visible && (key == InputConstants.KEY_TAB || key == InputConstants.KEY_RETURN);
    }

    public String getSelected() {
        if (!visible || suggestions.isEmpty()) return null;
        return suggestions.get(selectedIndex);
    }

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        visible = false;
    }

    public void render(GuiGraphicsExtractor context, int cursorPixelX, int cursorPixelY, int scissorX, int scissorW) {
        if (!visible || suggestions.isEmpty()) return;

        int popupH = suggestions.size() * ROW_H + PADDING * 2;
        int px = Mth.clamp(cursorPixelX, scissorX, scissorX + scissorW - POPUP_W);
        int py = cursorPixelY + ROW_H + 1;

        RenderUtils.fillRoundRect(context, px, py, POPUP_W, popupH, 3, COLOR_BG);
        RenderUtils.fillRoundShadow(context, px, py, POPUP_W, popupH, 3, 1, COLOR_BORDER, COLOR_BORDER);

        for (int i = 0; i < suggestions.size(); i++) {
            int rowY = py + PADDING + i * ROW_H;
            if (i == selectedIndex) {
                RenderUtils.fillRect(context, px + 1, rowY - 1, POPUP_W - 2, ROW_H, COLOR_SELECTED);
            }
            int color = i == selectedIndex ? COLOR_HIGHLIGHT : COLOR_TEXT;
            RenderUtils.drawDefaultScaledText(context, Component.literal(suggestions.get(i)), px + PADDING, rowY, 0.8F, false, color);
        }
    }
}
