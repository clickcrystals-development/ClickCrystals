package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.modules.scripts.TargetType;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public class DamageCmd extends ScriptCommand implements ThenChainable {

    public static final Predicate<Entity> ENTITY_EXISTS = ent -> ent instanceof LivingEntity && ent.isAlive();
  
    public DamageCmd() {
        super("damage");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.interactionManager == null) {
            return;
        }

        switch (args.get(0).toEnum(TargetType.class, null)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(32, filter, entity -> mc.interactionManager.attackEntity(mc.player, entity));
                executeWithThen(args, 2);
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(32, ENTITY_EXISTS, entity -> mc.interactionManager.attackEntity(mc.player, entity));
                executeWithThen(args, 1);
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }
}