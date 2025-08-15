package io.github.itzispyder.clickcrystals.scripting.syntax;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;

public interface ThenChainable {

    default void executeWithThen(ScriptArgs args, int beginIndex) {
        args.executeAll(args.match(beginIndex, "then") ? beginIndex + 1 : beginIndex);
    }

    default void executeWithThen(ScriptArgs args) {
        executeWithThen(args, 0);
    }
}
