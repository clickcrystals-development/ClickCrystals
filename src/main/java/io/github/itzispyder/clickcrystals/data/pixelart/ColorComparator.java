package io.github.itzispyder.clickcrystals.data.pixelart;

import java.awt.*;

public final class ColorComparator {

    public static double compare(Color a, Color b) {
        int r1 = a.getRed();
        int g1 = a.getGreen();
        int b1 = a.getBlue();
        int r2 = b.getRed();
        int g2 = b.getGreen();
        int b2 = b.getBlue();
        int rs = (r2 - r1) * (r2 - r1);
        int gs = (g2 - g1) * (g2 - g1);
        int bs = (b2 - b1) * (b2 - b1);
        return Math.abs(Math.sqrt(rs + gs + bs));
    }

    public static double compare(int a, int b) {
        return compare(new Color(a, true), new Color(b, true));
    }
}
