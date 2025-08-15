package io.github.itzispyder.clickcrystals.scripting.components;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.syntax.logic.AsCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;

public class ConditionEvaluationContext {

    public final Entity entity;
    public final ScriptArgs args;

    private final int beginPosition;
    private int endPosition;
    private boolean ended;

    public ConditionEvaluationContext(Entity entity, ScriptArgs args, int beginPosition) {
        this.entity = entity;
        this.args = args;
        this.beginPosition = beginPosition;
        this.endPosition = beginPosition;
    }

    public ScriptArgs.Arg get(int i) {
        int pointer = beginPosition + 1 + i; // +1 because get(0) is conditional name
        ScriptArgs.Arg arg = args.get(pointer);
        if (pointer > endPosition)
            endPosition = pointer;
        return arg;
    }

    public boolean match(int i, String arg) {
        int pointer = beginPosition + 1 + i; // +1 because get(0) is conditional name
        boolean bl = args.match(pointer, arg);
        if (pointer > endPosition)
            endPosition = pointer;
        return bl;
    }

    public ConditionEvaluationResult end(boolean requiresDirectReference, boolean value) {
        if (ended)
            throw new IllegalStateException("condition context was already killed");
        ended = true;
        args.zeroCursor(endPosition + 1); // +1 because we want the position of the next argument
        return new ConditionEvaluationResult(requiresDirectReference, value);
    }

    public ConditionEvaluationResult end(boolean value) {
        return end(false, value);
    }

    // helper methods

    public ConditionEvaluationContext assertClientPlayer() {
        if (AsCmd.getCurrentReferenceEntity() != PlayerUtils.player())
            throw new IllegalArgumentException("unsupported action on non-client player or entity; did you unintentionally use the 'as' command?");
        return this;
    }

    public boolean compareNumArg(int arg, double with) {
        return ComparatorOperator.eval(with, get(arg).toString());
    }
}
