package io.github.itzispyder.clickcrystals.scripting.components;

@FunctionalInterface
public interface Conditional {

    /**
     * Function interface that represents an abstract conditional
     * @param context the current evaluation context
     * @return the evaluation result
     */
    ConditionEvaluationResult evaluate(ConditionEvaluationContext context);
}