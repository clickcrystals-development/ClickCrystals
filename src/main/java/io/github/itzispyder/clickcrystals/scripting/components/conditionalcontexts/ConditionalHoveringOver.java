package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.mixins.AccessorHandledScreen;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.awt.*;
import java.util.function.Predicate;

public class ConditionalHoveringOver implements Conditional, Global {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        ClientPlayerEntity p = PlayerUtils.player();
        Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
        if (p == null || p.currentScreenHandler == null || !(mc.currentScreen instanceof HandledScreen<?> handle))
            return ctx.end(false);

        AccessorHandledScreen screen = (AccessorHandledScreen) handle;
        Point cursor = InteractionUtils.getCursor();

        for (Slot slot : p.currentScreenHandler.slots) {
            if (!screen.isHovered(slot, cursor.x, cursor.y))
                continue;
            ItemStack stack = slot.getStack();
            if (stack == null || !item.test(stack))
                continue;
            return ctx.end(true);
        }
        return ctx.end(false);
    }
}
