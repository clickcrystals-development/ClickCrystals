package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

public class ConditionCmd extends ScriptCommand implements Global {

    public ConditionCmd() {
        super("condition");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ConditionType type = args.get(0).enumValue(ConditionType.class, null);
        String toExecute = args.getAll(2).stringValue();

        switch (type) {
            case HOLDING -> {
                if (mc != null && PlayerUtils.playerNotNull()) {
                    if (OnEventCmd.parseItemPredicate(args.get(1).stringValue()).test(HotbarUtils.getHand())) {
                        ClickScript.executeOneLine(toExecute);
                    }
                }
            }
            case TARGET_BLOCK -> {
                if (mc != null && PlayerUtils.playerNotNull() && mc.crosshairTarget instanceof BlockHitResult hit) {
                    if (OnEventCmd.parseBlockPredicate(args.get(1).stringValue()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()))) {
                        ClickScript.executeOneLine(toExecute);
                    }
                }
            }
            case TARGET_ENTITY -> {
                if (mc != null && PlayerUtils.playerNotNull() && mc.crosshairTarget instanceof EntityHitResult hit) {
                    if (OnEventCmd.parseEntityPredicate(args.get(1).stringValue()).test(hit.getEntity())) {
                        ClickScript.executeOneLine(toExecute);
                    }
                }
            }
        }
    }

    public enum ConditionType {
        HOLDING,
        TARGET_BLOCK,
        TARGET_ENTITY
    }
}
