package io.github.itzispyder.clickcrystals.client.clickscript;

import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandLine;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
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
        this.put("uncharged_respawn_anchor", state -> state.isOf(Blocks.RESPAWN_ANCHOR) && state.get(RespawnAnchorBlock.CHARGES) < 1);
        this.put("charged_respawn_anchor", state -> state.isOf(Blocks.RESPAWN_ANCHOR) && state.get(RespawnAnchorBlock.CHARGES) > 0);
    }};

    public static Predicate<ItemStack> parseItemPredicate(String arg) {
        Predicate<String> filter = s -> s != null && s.trim().length() > 1;
        List<Predicate<ItemStack>> list = new ArrayList<>();

        for (String section : Arrays.stream(arg.split(",")).filter(filter).toArray(String[]::new)) {
            if (section.startsWith("#")) {
                list.add(item -> item.getItem().getTranslationKey().contains(section.substring(1)));
            }
            else if (section.startsWith(":")) {
                Identifier id = new Identifier("minecraft", section.substring(1));
                list.add(item -> item.getItem() == Registries.ITEM.get(id));
            }
            else {
                list.add(item -> false);
            }
        }
        return item -> list.stream().anyMatch(pre -> pre.test(item));
    }

    public static Predicate<BlockState> parseBlockPredicate(String arg) {
        Predicate<String> filter = s -> s != null && s.trim().length() > 1;
        List<Predicate<BlockState>> list = new ArrayList<>();

        for (String section : Arrays.stream(arg.split(",")).filter(filter).toArray(String[]::new)) {
            if (section.startsWith("#")) {
                list.add(block -> block.getBlock().getTranslationKey().contains(section.substring(1)));
            }
            else if (section.startsWith(":")) {
                String subArg = section.substring(1);

                if (defaultedBlockPredicates.containsKey(subArg)) {
                    return defaultedBlockPredicates.get(subArg);
                }

                Identifier id = new Identifier("minecraft", subArg);
                return block -> block.getBlock() == Registries.BLOCK.get(id);
            }
            else {
                list.add(block -> false);
            }
        }
        return block -> list.stream().anyMatch(pre -> pre.test(block));
    }

    public static Predicate<Entity> parseEntityPredicate(String arg) {
        Predicate<String> filter = s -> s != null && s.trim().length() > 1;
        List<Predicate<Entity>> list = new ArrayList<>();

        for (String section : Arrays.stream(arg.split(",")).filter(filter).toArray(String[]::new)) {
            if (section.startsWith("#")) {
                list.add(ent -> ent.getType().getTranslationKey().contains(section.substring(1)));
            }
            else if (section.startsWith(":")) {
                Identifier id = new Identifier("minecraft", section.substring(1));
                list.add(ent -> ent.getType() == Registries.ENTITY_TYPE.get(id));
            }
            else {
                list.add(item -> false);
            }
        }
        return item -> list.stream().anyMatch(pre -> pre.test(item));
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
}
