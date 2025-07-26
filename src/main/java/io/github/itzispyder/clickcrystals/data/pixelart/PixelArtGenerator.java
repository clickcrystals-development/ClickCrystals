package io.github.itzispyder.clickcrystals.data.pixelart;

import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class PixelArtGenerator {

    private static final AtomicBoolean running = new AtomicBoolean(false);
    private final BufferedImage image;
    private final ImageEditor editor;
    private long genInterval;
    private Pixel[][] pixels;

    public PixelArtGenerator(BufferedImage image, long genInterval) {
        this.image = image;
        this.editor = new ImageEditor(this.image);
        this.genInterval = genInterval;
        this.fillPixels(this.image.getWidth(), this.image.getHeight());
    }

    public PixelArtGenerator(ImageEditor editor, long genInterval) {
        this.image = editor.getImage();
        this.editor = editor;
        this.genInterval = genInterval;
        this.fillPixels(this.image.getWidth(), this.image.getHeight());
    }

    public static boolean isRunning() {
        return running.get();
    }

    public static boolean hasStopped() {
        return !running.get();
    }

    public static void cancel() {
        if (running.get()) {
            running.set(false);
        }
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

        running.set(true);
        return CompletableFuture.runAsync(() -> {
            for (int y = 0; y < image.getHeight(); y++) {
                genRow(y, world, pos, facing);
            }
            running.set(false);
        });
    }

    private synchronized void genRow(int y, World world, BlockPos pos, Facing facing) {
        Block prev = null;
        BlockPos c1 = pos;
        BlockPos c2 = c1;

        for (int x = 0; x < image.getWidth(); x++) {
            if (hasStopped()) break;

            Pixel p = pixels[x][y];
            Block curr = p.getBlock(world, pos, facing);
            BlockPos loc = p.getPos(pos, facing);

            if (prev == null) prev = curr;

            if (curr == prev && x < image.getWidth() - 1) {
                c2 = loc;
            }
            else {
                if (c1.getY() != c2.getY()) {
                    c1 = c1.withY(c2.getY());
                }

                String key = keyOf(prev);
                String pos1 = c1.toShortString().replaceAll(",", "");
                String pos2 = c2.toShortString().replaceAll(",", "");
                String cmd;

                if (pos1.equals(pos2)) {
                    cmd = "setblock " + pos1 + " " + key;
                }
                else {
                    cmd = "fill " + pos1 + " " + pos2 + " " + key;
                }

                ChatUtils.sendChatCommand(cmd);
                prev = curr;
                c1 = loc;
                c2 = loc;

                try {
                    Thread.sleep(genInterval * 50L);
                }
                catch (Exception ignore) {}
            }
        }
    }

    private String keyOf(Block block) {
        String[] ks = block.getTranslationKey().split("\\.");
        return ks[ks.length - 1];
    }

    public BufferedImage getImage() {
        return image;
    }

    public ImageEditor getEditor() {
        return editor;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getArea() {
        return getWidth() * getHeight();
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    public long getGenInterval() {
        return genInterval;
    }

    public void setGenInterval(long genInterval) {
        this.genInterval = genInterval;
    }

    private record Pixel(int x, int y, int color) {
        public static final List<Block> BLACKLIST = List.of(
                Blocks.ICE,
                Blocks.TNT,
                Blocks.REDSTONE_LAMP,
                Blocks.GRASS_BLOCK,
                Blocks.CRIMSON_NYLIUM,
                Blocks.WARPED_NYLIUM,
                Blocks.PODZOL,
                Blocks.DIRT_PATH,
                Blocks.MYCELIUM,
                Blocks.BARRIER,
                Blocks.TEST_BLOCK,
                Blocks.TEST_INSTANCE_BLOCK,
                Blocks.STRUCTURE_BLOCK
        );
        public static final List<String> BLACKLIST_KEYS = List.of(
                "cherry",
                "leaves",
                "glass",
                "command"
        );

        public synchronized Block getBlock(BlockView view, BlockPos pos, Facing facing) {
            Block mostSimilar = Blocks.AIR;
            double similarity = 999999999.0;
            int x = facing == Facing.NORTH_SOUTH ? pos.getX() + this.x : pos.getX();
            int y = pos.getX() + this.y;
            int z = facing == Facing.EAST_WEST ? pos.getZ() + this.x : pos.getZ();
            pos = new BlockPos(x, y, z);

            for (Block b : Registries.BLOCK) {
                if (!isValid(b, view, pos)) continue;
                double sim = ColorComparator.compare(color, b.getDefaultMapColor().color);
                if (sim < similarity) {
                    mostSimilar = b;
                    similarity = sim;
                }
            }

            return mostSimilar;
        }

        private boolean isValid(Block b, BlockView v, BlockPos p) {
            BlockState state = b.getDefaultState();
            boolean full = !state.isTransparent() && state.isFullCube(v, p);
            boolean type = !BLACKLIST.contains(b);
            boolean keys = BLACKLIST_KEYS.stream().noneMatch(b.getTranslationKey()::contains);
            return full && type && keys;
        }

        public synchronized BlockPos getPos(BlockPos pos, Facing facing) {
            int x = facing == Facing.NORTH_SOUTH ? pos.getX() + this.x : pos.getX();
            int y = pos.getY() + this.y;
            int z = facing == Facing.EAST_WEST ? pos.getZ() + this.x : pos.getZ();

            return new BlockPos(x, y, z);
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

    public static synchronized void generateImage(String stringUrl, long delay, Consumer<ImageEditor> edits) {
        ClientPlayerEntity p = PlayerUtils.player();

        if (!p.isCreative()) {
            Command.error("Please refrain from using this in any gamemode other than creative!");
            return;
        }
        if (running.get()) {
            Command.error("A pixel art is already generating!");
            return;
        }

        ImageEditor e;
        int w, h, a;

        try {
            e = ImageEditor.openFromUrl(new URL(stringUrl));
            edits.accept(e);
            w = e.getWidth();
            h = e.getHeight();
            a = e.getArea();
        }
        catch (Exception ex) {
            Command.error(ex.getMessage());
            return;
        }

        PixelArtGenerator gen = new PixelArtGenerator(e, delay);
        Timer timer = Timer.start();
        Command.info("&bGenerating &7" + a + "&b blocks &7(" + w + " x " + h + ")&b at position &7[" + p.getBlockPos().toShortString() + "]");
        CompletableFuture<Void> future = gen.generateAt(p);

        future.thenRun(() -> {
            if (future.isDone()) {
                Timer.End end = timer.end();
                Command.info("&bGeneration finished in &7" + end.getStampPrecise());
            }
        });
    }
}
