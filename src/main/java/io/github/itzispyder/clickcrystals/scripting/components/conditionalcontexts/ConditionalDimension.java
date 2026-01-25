package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.misc.Dimensions;

public class ConditionalDimension implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        boolean bl = false;
        switch (ctx.get(0).toEnum(Dimensions.class, null)) {
            case OVERWORLD -> bl = Dimensions.isOverworld();
            case THE_NETHER -> bl = Dimensions.isNether();
            case THE_END -> bl = Dimensions.isEnd();
        }
        return ctx.end(bl);
    }
}
