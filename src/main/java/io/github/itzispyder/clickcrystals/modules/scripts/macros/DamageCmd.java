package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public class DamageCmd extends ScriptCommand {

    public static final Predicate<Entity> ENTITY_EXISTS = ent -> ent instanceof LivingEntity && ent.isAlive();
  
    public DamageCmd() {
        super("damage");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.interactionManager == null) {
            return;
        }

        switch (args.get(0).toEnum(Mode.class, null)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = OnEventCmd.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(32, filter, entity -> mc.interactionManager.attackEntity(mc.player, entity));
                OnEventCmd.executeWithThen(args, 2);
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(32, ENTITY_EXISTS, entity -> mc.interactionManager.attackEntity(mc.player, entity));
                OnEventCmd.executeWithThen(args, 1);
            }
        }
    }

    public enum Mode {
        NEAREST_ENTITY,
        ANY_ENTITY
    }
}