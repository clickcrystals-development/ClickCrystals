package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import net.minecraft.command.CommandSource;

import java.util.stream.Collectors;

public class TableGenerator extends Command {

    public TableGenerator() {
        super("table-generator", "Generates a table with modules & scripts.", ",table-generator modules");
    }


    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("modules")
                .executes(context -> {
                    generateModulesTable();
                    return SINGLE_SUCCESS;
                }));
    }


    void generateModulesTable() {
        String moduleChart = system.collectModules().stream()
                .map(module -> String.format("| %s | %s |",
                        module.getName(),
                        module.getDescription() != null ? module.getDescription() : ""))
                .collect(Collectors.joining("\n"));

        String commandChart = system.commands().values().stream()
                .map(command -> String.format("| %s | %s |",
                        command.getName(),
                        command.getDescription() != null ? command.getDescription() : ""))
                .collect(Collectors.joining("\n"));

        String chart = """
                ## Modules and Commands
               
                ### Modules:
                | Name          | Description       |
                |---------------|-------------------|
                %s
                
                ### Commands:
                | Name          | Description       |
                |---------------|-------------------|
                %s
                """.formatted(moduleChart, commandChart);

        mc.keyboard.setClipboard(chart);

        info("Modules and scripts chart has been copied to your clipboard!");
    }
}
