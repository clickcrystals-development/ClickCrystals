package io.github.itzispyder.clickcrystals.gui.misc;

/**
 * Central theme system for the ClickCrystals client UI.
 *
 * All accent / brand colors are derived from a single user-configurable
 * primary color.  Neutral structural colors (grays, black, white) are
 * never altered.
 *
 * Default primary: #00B7FF — exactly matches the original ClickCrystals
 * appearance so users who never change the theme see no difference.
 *
 * Usage:
 *   // instead of  0xFF00B7FF  →  ClientTheme.primary()
 *   // instead of  0x8000B7FF  →  ClientTheme.primaryHalf()
 *   // instead of  0x0000B7FF  →  ClientTheme.primaryClear()
 *   // instead of  Shades.GENERIC      →  ClientTheme.primaryLight()
 *   // instead of  Shades.GENERIC_LOW  →  ClientTheme.primaryDim()
 */
public final class ClientTheme {

    // ------------------------------------------------------------------
    //  Default primary — preserves original ClickCrystals blue exactly
    // ------------------------------------------------------------------
    public static final int DEFAULT_PRIMARY = 0xFF00B7FF;

    /** Exact original GENERIC shade (kept for the default case only). */
    private static final int DEFAULT_LIGHT = 0xFF73D4FF;

    /** Exact original GENERIC_LOW shade (kept for the default case only). */
    private static final int DEFAULT_DIM   = 0xFF3873A9;

    private static int currentPrimary = DEFAULT_PRIMARY;

    private ClientTheme() {}

    // ------------------------------------------------------------------
    //  Themed color accessors — call these instead of hardcoded literals
    // ------------------------------------------------------------------

    /** Full-opacity primary accent color (e.g. glowing border). */
    public static int primary() {
        return currentPrimary;
    }

    /** 50%-alpha primary (e.g. transparent shadow glow). */
    public static int primaryHalf() {
        return (currentPrimary & 0x00FFFFFF) | 0x80000000;
    }

    /** 0%-alpha primary (fade-out end of gradients). */
    public static int primaryClear() {
        return currentPrimary & 0x00FFFFFF;
    }

    /**
     * Lighter, desaturated variant of the primary.
     * Replaces {@link Shades#GENERIC}.
     */
    public static int primaryLight() {
        if (currentPrimary == DEFAULT_PRIMARY) return DEFAULT_LIGHT;
        return deriveVariant(currentPrimary, 0.549f, 1.0f);
    }

    /**
     * Darker, muted variant of the primary.
     * Replaces {@link Shades#GENERIC_LOW}.
     */
    public static int primaryDim() {
        if (currentPrimary == DEFAULT_PRIMARY) return DEFAULT_DIM;
        return deriveVariant(currentPrimary, 0.669f, 0.663f);
    }

    /** 50%-alpha version of {@link #primaryLight()} — replaces {@link Shades#TRANS_GENERIC}. */
    public static int primaryLightHalf() {
        return (primaryLight() & 0x00FFFFFF) | 0x80000000;
    }

    /** 50%-alpha version of {@link #primaryDim()} — replaces {@link Shades#TRANS_GENERIC_LOW}. */
    public static int primaryDimHalf() {
        return (primaryDim() & 0x00FFFFFF) | 0x80000000;
    }

    // ------------------------------------------------------------------
    //  Theme management
    // ------------------------------------------------------------------

    /**
     * Apply a new client theme color.  Pass any ARGB int — the alpha
     * component is forced to 0xFF so all derived variants work correctly.
     * All derived colors update automatically and {@link Shades} is kept
     * in sync.
     */
    public static void setTheme(int argb) {
        currentPrimary = 0xFF000000 | (argb & 0x00FFFFFF);
        syncShades();
    }

    /** Convenience overload accepting a {@link Color} object. */
    public static void setTheme(Color color) {
        setTheme(color.getHexOpaque());
    }

    /** Reset to the built-in default (the original ClickCrystals blue). */
    public static void resetToDefault() {
        setTheme(DEFAULT_PRIMARY);
    }

    /** Returns the current primary as a {@link Color} object. */
    public static Color getThemeColor() {
        return new Color(currentPrimary);
    }

    /**
     * Returns the current primary formatted as {@code "#RRGGBB"} for
     * persistence in the config file.
     */
    public static String getThemeHex() {
        return String.format("#%06X", currentPrimary & 0x00FFFFFF);
    }

    /**
     * Returns the nearest Minecraft legacy §-color-code string (e.g. {@code "§b"})
     * for the current theme primary color.  Useful where true-RGB styling is not
     * available (chat message text, module toggle notifications, etc.).
     *
     * The 16 Minecraft chat-color RGB values are compared to the current primary
     * via squared Euclidean distance; the closest match is returned.
     * For the default theme this always returns {@code "§b"} (aqua) — no change.
     */
    public static String themedChatCode() {
        int r = (currentPrimary >> 16) & 0xFF;
        int g = (currentPrimary >> 8)  & 0xFF;
        int b =  currentPrimary        & 0xFF;
        // Minecraft chat colors in order §0-§f
        int[][] chatColors = {
            {  0,   0,   0}, {  0,   0, 170}, {  0, 170,   0}, {  0, 170, 170},
            {170,   0,   0}, {170,   0, 170}, {255, 170,   0}, {170, 170, 170},
            { 85,  85,  85}, { 85,  85, 255}, { 85, 255,  85}, { 85, 255, 255},
            {255,  85,  85}, {255,  85, 255}, {255, 255,  85}, {255, 255, 255}
        };
        String codes = "0123456789abcdef";
        int bestIndex = 11; // §b aqua — matches the default #00B7FF theme
        int bestDist = Integer.MAX_VALUE;
        for (int i = 0; i < chatColors.length; i++) {
            int dr = r - chatColors[i][0];
            int dg = g - chatColors[i][1];
            int db = b - chatColors[i][2];
            int dist = dr * dr + dg * dg + db * db;
            if (dist < bestDist) {
                bestDist = dist;
                bestIndex = i;
            }
        }
        return "§" + codes.charAt(bestIndex);
    }

    // ------------------------------------------------------------------
    //  Internal helpers
    // ------------------------------------------------------------------

    /**
     * Derive a themed color by adjusting the HSB saturation and
     * brightness of the primary while preserving its hue.
     *
     * @param argb    base ARGB color
     * @param sFactor multiplier for saturation  [0, 1]
     * @param bFactor multiplier for brightness  [0, 1]
     */
    private static int deriveVariant(int argb, float sFactor, float bFactor) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8)  & 0xFF;
        int b =  argb        & 0xFF;
        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
        int rgb = java.awt.Color.HSBtoRGB(
                hsb[0],
                Math.min(hsb[1] * sFactor, 1.0f),
                Math.min(hsb[2] * bFactor, 1.0f)
        );
        return 0xFF000000 | (rgb & 0x00FFFFFF);
    }

    /** Keep {@link Shades} mutable accent fields and {@link io.github.itzispyder.clickcrystals.gui.hud.Hud#DEFAULT_COLOR} in sync with the current theme. */
    private static void syncShades() {
        Shades.GENERIC           = primaryLight();
        Shades.GENERIC_LOW       = primaryDim();
        Shades.TRANS_GENERIC     = primaryLightHalf();
        Shades.TRANS_GENERIC_LOW = primaryDimHalf();
        io.github.itzispyder.clickcrystals.gui.hud.Hud.syncDefaultColor();
    }
}
