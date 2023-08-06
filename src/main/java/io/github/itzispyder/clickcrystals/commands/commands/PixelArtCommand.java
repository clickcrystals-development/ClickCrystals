package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.pixelart.PixelArtGenerator;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.Timer;
import net.minecraft.command.CommandSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class PixelArtCommand extends Command {

    private static final AtomicBoolean running = new AtomicBoolean();

    public PixelArtCommand() {
        super("pixelart", "Generates pixel art.", "/pixelart");
        running.set(false);
    }

    public static boolean isRunning() {
        return running.get();
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("cancel")
                        .executes(context -> {
                            if (running.get()) {
                                running.set(false);
                                info("Cancelled 1 generation task!");
                            }
                            else {
                                error("No pixel art is generating");
                            }
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("generate").then(argument("url", StringArgumentType.greedyString())
                        .executes(context -> {
                            String url = context.getArgument("url", String.class);
                            mc.execute(() -> generateImage(url));
                            return SINGLE_SUCCESS;
                        })));
    }

    public void generateImage(String url) {
        URL ur;
        BufferedImage bi;

        try {
            ur = new URL(url);
            InputStream is = ur.openStream();
            bi = ImageIO.read(is);
        }
        catch (Exception ex) {
            error(ex.getMessage());
            return;
        }

        if (running.get()) {
            error("A pixel art is already generating!");
            return;
        }

        PixelArtGenerator gen;
        int w, h;

        try {
            gen = new PixelArtGenerator(bi);
            w = bi.getWidth();
            h = bi.getHeight();
        }
        catch (Exception ex) {
            error(ex.getMessage());
            return;
        }

        info("&bGenerating &7" + (w * h) + "&b blocks &7(" + w + " x " + h + ")&b at position &7[" + PlayerUtils.player().getBlockPos().toShortString() + "]");
        Timer timer = Timer.start();
        running.set(true);
        CompletableFuture<Void> future = gen.generateAt(PlayerUtils.player());

        future.thenRun(() -> {
            if (future.isDone()) {
                Timer.End end = timer.end();
                running.set(false);
                info("&bGeneration finished in &7" + end.getStampPrecise());
            }
        });
    }
}
