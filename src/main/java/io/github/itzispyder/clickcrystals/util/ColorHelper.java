package io.github.itzispyder.clickcrystals.util;

import net.minecraft.util.math.MathHelper;

public class ColorHelper {
    public static class Argb {
        public Argb() {
        }

        public static int getAlpha(int argb) {
            return argb >>> 24;
        }

        public static int getRed(int argb) {
            return argb >> 16 & 255;
        }

        public static int getGreen(int argb) {
            return argb >> 8 & 255;
        }

        public static int getBlue(int argb) {
            return argb & 255;
        }

        public static int getArgb(int alpha, int red, int green, int blue) {
            return alpha << 24 | red << 16 | green << 8 | blue;
        }

        public static int getArgb(int red, int green, int blue) {
            return getArgb(255, red, green, blue);
        }

        public static int mixColor(int first, int second) {
            return getArgb(getAlpha(first) * getAlpha(second) / 255, getRed(first) * getRed(second) / 255, getGreen(first) * getGreen(second) / 255, getBlue(first) * getBlue(second) / 255);
        }

        public static int lerp(float delta, int start, int end) {
            int i = MathHelper.lerp(delta, getAlpha(start), getAlpha(end));
            int j = MathHelper.lerp(delta, getRed(start), getRed(end));
            int k = MathHelper.lerp(delta, getGreen(start), getGreen(end));
            int l = MathHelper.lerp(delta, getBlue(start), getBlue(end));
            return getArgb(i, j, k, l);
        }

        public static int fullAlpha(int argb) {
            return argb | -16777216;
        }

        public static int withAlpha(int alpha, int rgb) {
            return alpha << 24 | rgb & 16777215;
        }

        public static int fromFloats(float a, float r, float g, float b) {
            return getArgb(net.minecraft.util.math.ColorHelper.channelFromFloat(a), net.minecraft.util.math.ColorHelper.channelFromFloat(r), net.minecraft.util.math.ColorHelper.channelFromFloat(g), net.minecraft.util.math.ColorHelper.channelFromFloat(b));
        }

        public static int averageArgb(int a, int b) {
            return getArgb((getAlpha(a) + getAlpha(b)) / 2, (getRed(a) + getRed(b)) / 2, (getGreen(a) + getGreen(b)) / 2, (getBlue(a) + getBlue(b)) / 2);
        }
    }

    public static class Abgr {
        public Abgr() {
        }

        public static int getAlpha(int abgr) {
            return abgr >>> 24;
        }

        public static int getRed(int abgr) {
            return abgr & 255;
        }

        public static int getGreen(int abgr) {
            return abgr >> 8 & 255;
        }

        public static int getBlue(int abgr) {
            return abgr >> 16 & 255;
        }

        public static int getBgr(int abgr) {
            return abgr & 16777215;
        }

        public static int toOpaque(int abgr) {
            return abgr | -16777216;
        }

        public static int getAbgr(int a, int b, int g, int r) {
            return a << 24 | b << 16 | g << 8 | r;
        }

        public static int withAlpha(int alpha, int bgr) {
            return alpha << 24 | bgr & 16777215;
        }

        public static int toAbgr(int argb) {
            return argb & -16711936 | (argb & 16711680) >> 16 | (argb & 255) << 16;
        }
    }
}
