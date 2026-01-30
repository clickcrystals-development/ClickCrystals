package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ConditionalInventorySlot implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext context) {
        if (PlayerUtils.invalid())
            return context.end(false);

        int slot = context.get(0).toInt();
        Predicate<ItemStack> item = ScriptParser.parseItemPredicate(context.get(1).toString());
        ItemStack stack = InvUtils.inv().getStack(slot);
        return context.assertClientPlayer().end(item.test(stack));
    }
}
