package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;

public interface ThenChainable {

    default void executeWithThen(ScriptArgs args, int beginIndex) {
        args.executeAll(args.match(beginIndex, "then") ? beginIndex + 1 : beginIndex);
    }
}
