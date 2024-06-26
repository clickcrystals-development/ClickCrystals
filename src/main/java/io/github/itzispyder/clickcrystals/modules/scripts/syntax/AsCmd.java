package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.modules.scripts.macros.DamageCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;

import java.util.function.Predicate;

public class AsCmd extends ScriptCommand implements ThenChainable {

    private static Entity currentReference;

    public AsCmd() {
        super("as");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        resetReferenceEntity();
        switch (args.get(0).toEnum(Mode.class, null)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(32, filter, entity -> currentReference = entity);
                executeWithThen(args, 2);
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(32, DamageCmd.ENTITY_EXISTS, entity -> currentReference = entity);
                executeWithThen(args, 1);
            }
            case CLIENT -> {
                currentReference = PlayerUtils.player();
                executeWithThen(args, 1);
            }
        }
    }

    public static Entity getCurrentReferenceEntity() {
        if (currentReference == null || !currentReference.isAlive() || currentReference.isSpectator())
            return PlayerUtils.player();
        return currentReference;
    }

    public static void resetReferenceEntity() {
        currentReference = null;
    }

    public enum Mode {
        NEAREST_ENTITY,
        ANY_ENTITY,
        CLIENT
    }
}
