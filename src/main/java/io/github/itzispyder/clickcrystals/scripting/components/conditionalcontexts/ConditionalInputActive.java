package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

public class ConditionalInputActive implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        ctx.assertClientPlayer();
        InputType a = ctx.get(0).toEnum(InputType.class, null);
        if (a != InputType.KEY)
            return ctx.end(a.isActive());
        else
            return ctx.end(InteractionUtils.isKeyExtendedNamePressed(ctx.get(1).toString()));
    }
}
