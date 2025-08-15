package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.scripting.syntax.macros.DamageCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;

import java.util.function.Predicate;

// @Format as (any_entity|target_entity|client) {}
// @Format as nearest_entity <identifier> {}
public class AsCmd extends ScriptCommand implements ThenChainable {

    private static Entity currentReference;
    private static boolean referencing = false;

    public AsCmd() {
        super("as");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        resetReferenceEntity();
        switch (args.get(0).toEnum(TargetType.class, null)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> currentReference = entity);
                referencing = true;
                executeWithThen(args, 2);
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(128, DamageCmd.ENTITY_EXISTS, entity -> currentReference = entity);
                referencing = true;
                executeWithThen(args, 1);
            }
            case CLIENT -> {
                currentReference = null;
                referencing = false;
                executeWithThen(args, 1);
            }
            case TARGET_ENTITY -> {
                if (mc.crosshairTarget instanceof EntityHitResult hit)
                    currentReference = hit.getEntity();
                referencing = true;
                executeWithThen(args, 1);
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    public static Entity getCurrentReferenceEntity() {
        if (currentReference == null /*|| !currentReference.isAlive()*/ || currentReference.isSpectator())
            return PlayerUtils.player();
        return currentReference;
    }

    public static Entity getCurrentReferenceEntityDirect() {
        return currentReference;
    }

    public static boolean hasCurrentReferenceEntity() {
        return currentReference != null || !referencing;
    }

    public static void resetReferenceEntity() {
        currentReference = null;
        referencing = false;
    }
}
