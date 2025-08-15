package io.github.itzispyder.clickcrystals.scripting.components;

import io.github.itzispyder.clickcrystals.scripting.syntax.logic.AsCmd;

public class ConditionEvaluationResult {

    private final boolean requiresDirectReference;
    private boolean value;

    public ConditionEvaluationResult(boolean requiresDirectReference, boolean value) {
        this.requiresDirectReference = requiresDirectReference;
        this.value = value;
    }

    public ConditionEvaluationResult(boolean value) {
        this(false, value);
    }

    public boolean requiresDirectReference() {
        return requiresDirectReference;
    }

    public boolean getRawValue() {
        return value;
    }

    public boolean getValue() {
        boolean result = getRawValue();
        if (requiresDirectReference && !AsCmd.hasCurrentReferenceEntity())
            result = false;

        AsCmd.resetReferenceEntity();
        return result;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
