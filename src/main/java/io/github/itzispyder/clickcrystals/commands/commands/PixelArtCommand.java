package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.pixelart.ImageEditor;
import io.github.itzispyder.clickcrystals.data.pixelart.PixelArtGenerator;
import net.minecraft.command.CommandSource;

import java.util.function.Consumer;

public class PixelArtCommand extends Command {

    public PixelArtCommand() {
        super("pixelart", "Generates pixel art.", ",pixelart");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("cancel")
                        .executes(context -> {
                            if (PixelArtGenerator.isRunning()) {
                                PixelArtGenerator.cancel();
                                info("Cancelled 1 generation task!");
                            }
                            else {
                                error("No pixel art is generating");
                            }
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("gen").then(argument("delay", LongArgumentType.longArg())
                        .then(argument("url", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String url = context.getArgument("url", String.class);
                                    long delay = Math.max(context.getArgument("delay", Long.class), 0);
                                    mc.execute(() -> generateImage(url, delay, ImageEditor::scaleToBounds));
                                    return SINGLE_SUCCESS;
                                }))))
                .then(literal("gen-custom").then(argument("delay", LongArgumentType.longArg())
                        .then(argument("width", IntegerArgumentType.integer())
                                .then(argument("height", IntegerArgumentType.integer())
                                        .then(argument("url", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    String url = context.getArgument("url", String.class);
                                                    long delay = Math.max(context.getArgument("delay", Long.class), 0);
                                                    int w = context.getArgument("width", Integer.class);
                                                    int h = context.getArgument("height", Integer.class);
                                                    mc.execute(() -> generateImage(url, delay, img -> {
                                                        img.scaleToCustom(w, h);
                                                    }));
                                                    return SINGLE_SUCCESS;
                                                }))))))
                .then(literal("gen-square").then(argument("delay", LongArgumentType.longArg())
                        .then(argument("width", IntegerArgumentType.integer())
                                .then(argument("url", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String url = context.getArgument("url", String.class);
                                            long delay = Math.max(context.getArgument("delay", Long.class), 0);
                                            int w = context.getArgument("width", Integer.class);
                                            mc.execute(() -> generateImage(url, delay, img -> {
                                                img.scaleToCustom(w, w);
                                            }));
                                            return SINGLE_SUCCESS;
                                        })))))
                .then(literal("gen-scaled").then(argument("delay", LongArgumentType.longArg())
                        .then(argument("scale", DoubleArgumentType.doubleArg(0.0, 2.0))
                                .then(argument("url", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String url = context.getArgument("url", String.class);
                                            long delay = Math.max(context.getArgument("delay", Long.class), 0);
                                            double r = context.getArgument("scale", Double.class);
                                            mc.execute(() -> generateImage(url, delay, img -> {
                                                img.scaleToCustom((int)(r * img.getWidth()), (int)(r * img.getHeight()));
                                            }));
                                            return SINGLE_SUCCESS;
                                        })))))
                .then(literal("gen-custom-width").then(argument("delay", LongArgumentType.longArg())
                        .then(argument("width", IntegerArgumentType.integer())
                                .then(argument("url", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String url = context.getArgument("url", String.class);
                                            long delay = Math.max(context.getArgument("delay", Long.class), 0);
                                            int w = context.getArgument("width", Integer.class);
                                            mc.execute(() -> generateImage(url, delay, img -> {
                                                double r = w / (double)img.getWidth();
                                                int h = (int)(img.getHeight() * r);
                                                img.scaleToCustom(w, h);
                                            }));
                                            return SINGLE_SUCCESS;
                                        })))))
                .then(literal("gen-custom-height").then(argument("delay", LongArgumentType.longArg())
                        .then(argument("height", IntegerArgumentType.integer())
                                .then(argument("url", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String url = context.getArgument("url", String.class);
                                            long delay = Math.max(context.getArgument("delay", Long.class), 0);
                                            int h = context.getArgument("height", Integer.class);
                                            mc.execute(() -> generateImage(url, delay, img -> {
                                                double r = h / (double)img.getHeight();
                                                int w = (int)(img.getWidth() * r);
                                                img.scaleToCustom(w, h);
                                            }));
                                            return SINGLE_SUCCESS;
                                        })))));
    }

    public void generateImage(String url, long interval, Consumer<ImageEditor> edits) {
        PixelArtGenerator.generateImage(url, interval, edits);
    }
}
