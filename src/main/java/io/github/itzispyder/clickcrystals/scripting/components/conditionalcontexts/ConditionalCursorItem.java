package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import java.util.function.Predicate;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class ConditionalCursorItem implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        LocalPlayer p = PlayerUtils.player();
        Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
        if (p == null || p.containerMenu == null)
            return ctx.end(false);
        ItemStack stack = p.containerMenu.getCarried();
        return ctx.end(stack != null && item.test(stack));
    }
}
