package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;

public class InputCmd extends ScriptCommand implements Global {

    public InputCmd() {
        super("input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        args.get(0).enumValue(Action.class, null).run();
    }

    public enum Action {
        ATTACK(InteractionUtils::doAttack),
        USE(InteractionUtils::doUse),
        FORWARD(() -> TickEventListener.forward(500)),
        BACKWARD(() -> TickEventListener.backward(500)),
        STRAFE_LEFT(() -> TickEventListener.strafeLeft(500)),
        STRAFE_RIGHT(() -> TickEventListener.strafeRight(500));

        private final Runnable action;

        Action(Runnable action) {
            this.action = action;
        }

        public void run() {
            if (mc != null && mc.options != null) {
                action.run();
            }
        }
    }
}
