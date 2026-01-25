package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ConditionalCursorItem implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        ClientPlayerEntity p = PlayerUtils.player();
        Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
        if (p == null || p.currentScreenHandler == null)
            return ctx.end(false);
        ItemStack stack = p.currentScreenHandler.getCursorStack();
        return ctx.end(stack != null && item.test(stack));
    }
}
