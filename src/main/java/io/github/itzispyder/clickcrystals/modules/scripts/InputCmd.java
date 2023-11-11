package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;
import net.minecraft.client.option.KeyBinding;

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
        FORWARD(() -> press(mc.options.forwardKey)),
        BACKWARD(() -> press(mc.options.backKey)),
        STRAFE_LEFT(() -> press(mc.options.leftKey)),
        STRAFE_RIGHT(() -> press(mc.options.rightKey));

        private final Runnable action;

        Action(Runnable action) {
            this.action = action;
        }

        private static void press(KeyBinding binding) {
            if (!binding.isPressed()) {
                binding.setPressed(true);
                system.scheduler.runDelayedTask(() -> binding.setPressed(false), 690L);
            }
        }

        public void run() {
            if (mc != null && mc.options != null) {
                action.run();
            }
        }
    }
}
