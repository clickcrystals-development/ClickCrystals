package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public class DamageCmd extends ScriptCommand {
  
  public DamageCmd() {
    super("damage");
  }
  
  @Override
  public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
    switch (args.get(0).toEnum(Mode.class, null)) {
      case NEAREST_ENTITY -> {
        Predicate<Entity> filter = OnEventCmd.parseEntityPredicate(args.get(1).toString());
        PlayerUtils.runOnNearestEntity(32, filter, entity -> mc.interactionManager.attackEntity(mc.player, entity));
      }
      case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(32, entity -> entity instanceof LivingEntity, entity -> mc.interactionManager.attackEntity(mc.player, entity));
    }
  }
  
  public enum Mode {
    NEAREST_ENTITY,
    ANY_ENTITY
  }
}