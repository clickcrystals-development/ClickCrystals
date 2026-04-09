package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

// @Format hold_input <input> <num>
// @Format hold_input key ... <num>
// @Format hold_input cancel
public class HoldInputCmd extends ScriptCommand implements ThenChainable {

    public HoldInputCmd() {
        super("hold_input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.match(0, "cancel")) {
            TickEventListener.cancelTickInputs();
            return;
        }

        var read = args.getReader();
        InputType a = read.next(InputType.class);
        
        if (a == InputType.KEY) {
            String key = read.nextStr();
            long ms = (long)(read.next().toDouble() * 1000L);
            TickEventListener.holdKey(key, ms);
            read.executeThenChain();
            return;
        }
        
        // check for teammate protection on attack inputs
        if ((a == InputType.ATTACK || a == InputType.LEFT) && mc.hitResult instanceof EntityHitResult hit) {
            if (hit.getEntity() instanceof Player player && EntityUtils.shouldCancelCcsAttack(player)) {
                read.executeThenChain();
                return; // skip holding attack on teammate
            }
        }
        
        long ms = (long)(read.next().toDouble() * 1000L);

        if (a.isDummy())
            throw new IllegalArgumentException("unsupported operation, input '%s' cannot be held".formatted(a));

        a.runFor(ms);
        read.executeThenChain();
    }
}
