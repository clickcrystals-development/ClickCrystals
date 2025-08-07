package io.github.itzispyder.clickcrystals.scripting.components;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;

public record CommandLine(String line) {

    public CommandLine {
        line = line.trim();
        if (line.endsWith(";")) {
            line = line.substring(0, line.length() - 1);
        }
    }

    public boolean isDeep() {
        return ScriptParser.parse(line).size() > 1;
    }

    public WaitingState getWaitingState() {
        boolean regexMatches = line.matches("^wait(_random)?( [0-9.-]+){1,2}$");
        double[] wait = {};

        if (regexMatches) {
            String[] args = line.split(" ");
            wait = new double[args.length];

            for (int i = 1; i < args.length; i++)
                wait[i - 1] = Double.parseDouble(args[i]);
        }

        return new WaitingState(regexMatches, wait);
    }

    public void execute() {
        execute(ClickScript.DEFAULT_DISPATCHER);
    }

    public void execute(ClickScript executor) {
        ClickScript.executeSingle(executor, line);
    }

    public void executeDynamic() {
        ClickScript.executeDynamic(line);
    }

    public void executeDynamic(ClickScript executor) {
        ClickScript.executeDynamic(executor, line);
    }

    public record WaitingState(boolean waiting, double[] timeParams) {
        public double seconds() {
            switch (timeParams.length) {
                case 0 -> {
                    return 0.0;
                }
                case 1 -> {
                    return timeParams[0];
                }
                case 2 -> {
                    return timeParams[0] + Math.random() * (timeParams[1] - timeParams[0]);
                }
                default -> {
                    double randomSum = 0;
                    for (int i = 0; i < timeParams.length - 1; i++)
                        randomSum += timeParams[i] + Math.random() * (timeParams[i + 1] - timeParams[i]);
                    return randomSum / timeParams.length;
                }
            }
        }
    }
}
