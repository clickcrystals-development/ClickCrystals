package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;


public class FaqCommand extends Command {

    public FaqCommand() {
        super("faq", "Open the Q&A site page.", ",faq");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
            Text message = Text.literal("§7[§bClick§3Crystals§7] §Have some questions about ClickCrystals? §b§nPress here for answers!")
                    .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://clickcrystals.xyz/faq")));
            ChatUtils.sendRawText(message);
            return SINGLE_SUCCESS;
        });
    }
}
