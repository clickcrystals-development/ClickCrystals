package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;

// @Format input <input>
// @Format input key ...
public class InputCmd extends ScriptCommand implements Global, ThenChainable {

    public InputCmd() {
        super("input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var read = args.getReader();
        InputType a = read.next(InputType.class);
        if (a != InputType.KEY) {
            if ((a == InputType.ATTACK || a == InputType.LEFT) && mc.crosshairTarget instanceof EntityHitResult hit) {
                if (hit.getEntity() instanceof PlayerEntity player && EntityUtils.shouldCancelCcsAttack(player)) {
                    read.executeThenChain();
                    return; // check if teammate
                }
            }
            a.run();
            read.executeThenChain();
        }
        else {
            InteractionUtils.pressKeyExtendedName(read.nextStr());
            read.executeThenChain();
        }
    }

}
