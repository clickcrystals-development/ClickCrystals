package io.github.itzispyder.clickcrystals.gui.misc;

// §
public enum ChatColor {
    // 0-9
    BLACK("§0", 0xFF000000),
    DARK_BLUE("§1", 0xFF0000AA),
    DARK_GREEN("§2", 0xFF00AA00),
    DARK_AQUA("§3", 0xFF00AAAA),
    DARK_RED("§4", 0xFFAA0000),
    DARK_PURPLE("§5", 0xFFAA00AA),
    ORANGE("§6", 0xFFFFAA00),
    GRAY("§7", 0xFFAAAAAA),
    DARK_GRAY("§8", 0xFF555555),
    BLUE("§9", 0xFF5555FF),

    // a-f
    GREEN("§a", 0xFF55FF55),
    AQUA("§b", 0xFF55FFFF),
    RED("§c", 0xFFFF5555),
    PURPLE("§d", 0xFFFF55FF),
    YELLOW("§e", 0xFFFFFF55),
    WHITE("§f", 0xFFFFFFFF),

    // special
    OBFUSCATED("§k", 0x00000000),
    BOLD("§l", 0x00000000),
    STRIKETHROUGH("§m", 0x00000000),
    UNDERLINE("§n", 0x00000000),
    ITALIC("§o", 0x00000000),
    RESET("§r", 0x00000000);

    private final String mcColor;
    private final int hex;

    ChatColor(String mcColor, int hex) {
        this.mcColor = mcColor;
        this.hex = hex;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getHex() {
        return hex;
    }

    @Override
    public String toString() {
        return mcColor;
    }
}
