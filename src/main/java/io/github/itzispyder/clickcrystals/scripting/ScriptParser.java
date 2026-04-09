package io.github.itzispyder.clickcrystals.scripting;

import io.github.itzispyder.clickcrystals.scripting.components.CommandLine;
import io.github.itzispyder.clickcrystals.scripting.components.IdentifierSelection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.NbtUtils;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ScriptParser {

    public static List<String> getAllSections(String line, char openingChar, char closingChar) {
        List<String> lines = new ArrayList<>();
        int index = 0;
        var section = firstSectionWithIndex(line, openingChar, closingChar);

        while (!section.left.isEmpty()) {
            lines.add(section.left);
            index += section.right;
            section = firstSectionWithIndex(line.substring(index), openingChar, closingChar);
        }

        return lines;
    }

    public static String firstSection(String line, char enclosingChar) {
        return firstSection(line, enclosingChar, enclosingChar);
    }

    public static String firstSection(String line, char openingChar, char closingChar) {
        return firstSectionWithIndex(line, openingChar, closingChar).left;
    }

    public static Pair<String, Integer> firstSectionWithIndex(String line, char openingChar, char closingChar) {
        line = line == null ? "" : line;
        StringBuilder result = new StringBuilder();

        if (line.isEmpty()) {
            return Pair.of(line, 0);
        }

        char[] chars = line.toCharArray();
        boolean began = false;
        int toIgnore = 0;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            boolean skip = i > 0 && chars[i - 1] == '\\';

            if (c == openingChar && !skip) { // start of the quote
                if (began) {
                    if (openingChar != closingChar) { // support deep operations
                        toIgnore++;
                    }
                }
                else {
                    began = true;
                    continue;
                }
            }

            if (c == closingChar && !skip && began) { // end of the quote
                if (toIgnore-- > 0) {
                    result.append(c);
                    continue;
                }
                return Pair.of(result.toString().trim(), i + 1);
            }
            else if (c == '\\' && !skip) {
                continue;
            }

            if (began) {
                result.append(c);
            }
        }

        String r = result.toString().trim();
        if (r.isEmpty()) {
            return Pair.of(r, chars.length);
        }

        String msg = "unclosed enclosing chars %s%s to mark string section".formatted(
                String.valueOf(openingChar),
                String.valueOf(closingChar)
        );
        throw new IllegalArgumentException(msg);
    }

    // script specific

    public static String readFile(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                result.append(line);

                if (!line.isEmpty()) {
                    if (!line.endsWith("{") && !line.endsWith("}") && !line.endsWith(";")) {
                        result.append(";");
                    }
                    result.append(" ");
                }
            }

            br.close();
            return result.toString().trim();
        }
        catch (Exception ex) {
            return "";
        }
    }

    public static String condenseLines(String string) {
        try {
            StringBuilder result = new StringBuilder();

            for (String line : string.lines().toArray(String[]::new)) {
                line = line.trim();
                result.append(line);

                if (!line.isEmpty()) {
                    result.append(" ");
                }
            }

            return result.toString().trim();
        }
        catch (Exception ex) {
            return "";
        }
    }

    public static List<CommandLine> parse(String line) {
        line = line == null ? "" : line;
        List<CommandLine> lines = new ArrayList<>();

        StringBuilder temp = new StringBuilder();
        char[] chars = line.toCharArray();
        boolean inQuote = false;
        int i = 0;

        while (i < chars.length) {
            char c = chars[i];
            boolean skip = i > 0 && chars[i - 1] == '\\';

            if (c == '"' && !skip) {
                inQuote = !inQuote;
            }

            if (c == '{' && !skip && !inQuote) {
                String subLine = line.substring(i);
                var section = firstSectionWithIndex(subLine, '{', '}');

                String lead = temp.toString().trim();
                lines.add(new CommandLine((lead.isEmpty() ? "%s%s" : "%s {%s}").formatted(lead, section.left)));

                temp = new StringBuilder();
                i += section.right;
                continue;
            }
            if (c == ';' && !skip && !inQuote) {
                String subLine = temp.toString().trim();

                if (!subLine.isEmpty()) {
                    lines.add(new CommandLine(subLine));
                }

                temp = new StringBuilder();
                i++;
                continue;
            }
            else {
                temp.append(c);
            }
            i++;
        }

        String remaining = temp.toString().trim();
        if (!remaining.isEmpty()) {
            lines.add(new CommandLine(remaining));
        }

        if (inQuote) {
            throw new IllegalArgumentException("unclosed quotation marks scanned in script");
        }
        return lines;
    }

    // script predicates

    private static final Map<String, Predicate<BlockState>> defaultedBlockPredicates = new HashMap<>() {{
        this.put("uncharged_respawn_anchor", state -> state.is(Blocks.RESPAWN_ANCHOR) && state.getValue(RespawnAnchorBlock.CHARGE) < 1);
        this.put("charged_respawn_anchor", state -> state.is(Blocks.RESPAWN_ANCHOR) && state.getValue(RespawnAnchorBlock.CHARGE) > 0);
        this.put("water_source", state -> state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8);
        this.put("lava_source", state -> state.is(Blocks.LAVA) && state.getFluidState().getAmount() == 8);
        this.put("flowing_water", state -> state.is(Blocks.WATER) && state.getFluidState().getAmount() < 8);
        this.put("flowing_lava", state -> state.is(Blocks.LAVA) && state.getFluidState().getAmount() < 8);
    }};
    private static final Map<String, Predicate<ItemStack>> defaultedItemPredicates = new HashMap<>() {{
        this.put("uncharged_crossbow", item -> item.getItem() == Items.CROSSBOW && !CrossbowItem.isCharged(item));
        this.put("charged_crossbow", item -> item.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(item));
    }};

    public static ItemStack parseItemStack(String arg) {
        if ("mainhand".equals(arg)) {
            return HotbarUtils.getHand(InteractionHand.MAIN_HAND);
        }
        else if ("offhand".equals(arg)) {
            return HotbarUtils.getHand(InteractionHand.OFF_HAND);
        }
        else if (arg.matches("^[:#].*$")) {
            int slot = HotbarUtils.searchSlot(parseItemPredicate(arg));
            return InvUtils.inv().getItem(slot);
        }
        return null;
    }

    public static ItemStack parseItemStack(Player player, String arg) {
        if ("mainhand".equals(arg)) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        }
        else if ("offhand".equals(arg)) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        }
        else if (arg.matches("^[:#].*$")) {
            Predicate<ItemStack> test = parseItemPredicate(arg);
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack item = player.getItemInHand(hand);
                if (test.test(item))
                    return item;
            }
        }
        return null;
    }

    public static Predicate<ItemStack> parseItemPredicate(String arg) {
        IdentifierSelection<ItemStack> selection = IdentifierSelection.parse(arg, ItemStack.class);
        selection.setContainsStrategy(node -> item -> item.getItem().getDescriptionId().contains(node.name));
        selection.setEqualsStrategy(node -> {
            if (defaultedItemPredicates.containsKey(node.name))
                return defaultedItemPredicates.get(node.name);
            else
                return state -> state.getItem() == BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(node.name));
        });
        selection.setNodeTagsStrategy(node -> item -> node.tags.stream().allMatch(tag -> NbtUtils.hasPotionOrEnchant(item, tag)));
        return selection.getIdentifierPredicate();
    }

    public static Predicate<BlockState> parseBlockPredicate(String arg) {
        IdentifierSelection<BlockState> selection = IdentifierSelection.parse(arg, BlockState.class);
        selection.setContainsStrategy(node -> state -> state.getBlock().getDescriptionId().contains(node.name));
        selection.setEqualsStrategy(node -> {
            if (defaultedBlockPredicates.containsKey(node.name))
                return defaultedBlockPredicates.get(node.name);
            else
                return state -> state.getBlock() == BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(node.name));
        });
        return selection.getIdentifierPredicate();
    }

    public static Predicate<Entity> parseEntityPredicate(String arg) {
        IdentifierSelection<Entity> selection = IdentifierSelection.parse(arg, Entity.class);
        selection.setContainsStrategy(node -> ent -> ent.getType().getDescriptionId().contains(node.name));
        selection.setEqualsStrategy(node -> ent -> ent.getType() == BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace(node.name)));
        return selection.getIdentifierPredicate();
    }

    public static Predicate<SoundInstance> parseSoundInstancePredicate(String arg) {
        IdentifierSelection<SoundInstance> selection = IdentifierSelection.parse(arg, SoundInstance.class);
        selection.setContainsStrategy(node -> sound -> sound.getIdentifier().toLanguageKey().replace('.', '_').contains(node.name));
        selection.setEqualsStrategy(node -> sound -> sound.getIdentifier().equals(Identifier.withDefaultNamespace(node.name)));
        return selection.getIdentifierPredicate();
    }

    public static SoundEvent parseSoundEvent(String arg) {
        for (IdentifierSelection.Node node : IdentifierSelection.parse(arg, SoundEvent.class)) switch (node.type) {
            case CONTAINS -> {
                Identifier result = null;
                for (Identifier id : BuiltInRegistries.SOUND_EVENT.keySet()) if (id.getPath().contains(node.name)) {
                    result = id;
                    break;
                }
                return result == null ? null : BuiltInRegistries.SOUND_EVENT.getValue(result);
            }
            case EQUALS -> {
                return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.withDefaultNamespace(node.name));
            }
        }
        return null;
    }

    public static MobEffect parseStatusEffect(String arg) {
        for (IdentifierSelection.Node node : IdentifierSelection.parse(arg, MobEffect.class)) switch (node.type) {
            case CONTAINS -> {
                Identifier result = null;
                for (Identifier id : BuiltInRegistries.MOB_EFFECT.keySet()) if (id.getPath().contains(node.name)) {
                    result = id;
                    break;
                }
                return result == null ? null : BuiltInRegistries.MOB_EFFECT.getValue(result);
            }
            case EQUALS -> {
                return BuiltInRegistries.MOB_EFFECT.getValue(Identifier.withDefaultNamespace(node.name));
            }
        }
        return null;
    }
}
