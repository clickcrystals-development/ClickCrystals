package io.github.itzispyder.clickcrystals.data.pixelart;

import io.github.itzispyder.clickcrystals.commands.commands.PixelArtCommand;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class PixelArtGenerator {

    public static final int MAX_WIDTH = 128;
    public static final int MAX_HEIGHT = 64;
    private final BufferedImage image;
    private Pixel[][] pixels;

    public PixelArtGenerator(BufferedImage image) {
        this.image = scaleToBounds(image);
        this.fillPixels(this.image.getWidth(), this.image.getHeight());
    }

    public static BufferedImage scaleToBounds(BufferedImage before) {
        if (before.getWidth() > MAX_WIDTH) {
            int scaledWidth = Math.min(before.getWidth(), MAX_WIDTH);
            double scaleRatio = scaledWidth / (double)before.getWidth();
            int scaledHeight = (int)(before.getWidth() * scaleRatio);

            Image image = before.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
            return bufferImage(image);
        }
        else if (before.getHeight() > MAX_HEIGHT) {
            int scaledHeight = Math.min(before.getHeight(), MAX_HEIGHT);
            double scaleRatio = scaledHeight / (double)before.getHeight();
            int scaledWidth = (int)(before.getWidth() * scaleRatio);

            Image image = before.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
            return bufferImage(image);
        }
        else return before;
    }

    public static BufferedImage bufferImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        BufferedImage bImg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)bImg.getGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bImg;
    }

    public synchronized void fillPixels(int width, int height) {
        this.pixels = new Pixel[width][height];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                pixels[x][y] = new Pixel(x, y, image.getRGB(x, image.getHeight() - 1 - y));
            }
        }
    }

    public synchronized CompletableFuture<Void> generateAt(LivingEntity ent) {
        World world = ent.getWorld();
        BlockPos pos = ent.getBlockPos();
        Facing facing = Facing.fromDirection(ent.getMovementDirection());
        ChatUtils.sendChatCommand("gamerule sendCommandFeedback false");

        return CompletableFuture.runAsync(() -> {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (!PixelArtCommand.isRunning()) break;
                    try {
                        ChatUtils.sendChatCommand(pixels[x][y].placeBlock(world, pos, facing));
                        Thread.sleep(1);
                    }
                    catch (Exception ignore) {}
                }
            }
        });
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    private record Pixel(int x, int y, int color) {
        public synchronized Block getBlock(BlockView view, BlockPos pos, Facing facing) {
            Block mostSimilar = Blocks.AIR;
            double similarity = 999999999.0;
            int x = facing == Facing.NORTH_SOUTH ? pos.getX() + this.x : pos.getX();
            int y = pos.getX() + this.y;
            int z = facing == Facing.EAST_WEST ? pos.getZ() + this.x : pos.getZ();
            pos = new BlockPos(x, y, z);

            for (Block b : Registries.BLOCK) {
                if (!isValid(b, view, pos)) continue;
                double sim = ColorConverter.compare(color, b.getDefaultMapColor().color);
                if (sim < similarity) {
                    mostSimilar = b;
                    similarity = sim;
                }
            }

            return mostSimilar;
        }

        private boolean isValid(Block block, BlockView view, BlockPos pos) {
            boolean full = block.getDefaultState().isFullCube(view, pos) && !block.isTransparent(block.getDefaultState(), view, pos);
            boolean type = block != Blocks.ICE && block != Blocks.TNT && !block.getTranslationKey().contains("leaves");
            return full && type;
        }

        public synchronized String placeBlock(BlockView view, BlockPos pos, Facing facing) {
            String[] keys = getBlock(view, pos, facing).getTranslationKey().split("\\.");
            int x = facing == Facing.NORTH_SOUTH ? pos.getX() + this.x : pos.getX();
            int y = pos.getY() + this.y;
            int z = facing == Facing.EAST_WEST ? pos.getZ() + this.x : pos.getZ();

            return "setblock " + x + " " + y + " " + z + " " + keys[keys.length - 1];
        }
    }

    public enum Facing {
        NORTH_SOUTH,
        EAST_WEST;

        public static Facing fromDirection(Direction dir) {
            Facing f;
            switch (dir) {
                case EAST, WEST ->  f = EAST_WEST;
                default ->          f = NORTH_SOUTH;
            }
            return f;
        }
    }
}
