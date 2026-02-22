package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.TeamDetector;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityUtils implements Global {

    public static boolean isTargetValid() {
        return mc.targetedEntity != null;
    }

    public static boolean isMoving(Entity ref) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        return liv.sidewaysSpeed != 0 || liv.forwardSpeed != 0;
    }

    public static boolean isBlocking(Entity ref) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        if (!liv.isBlocking())
            return false;
            
        // Check if shield is facing the attacker
        if (PlayerUtils.invalid())
            return true;
            
        Vec3d targetLook = liv.getRotationVector().normalize();
        Vec3d toAttacker = PlayerUtils.player().getEntityPos().subtract(liv.getEntityPos()).normalize();
        double dot = targetLook.dotProduct(toAttacker);
        
        return dot > 0.3; // Shield blocks if target is facing attacker
    }

    public static boolean isColliding(Entity ref) {
        return ref.horizontalCollision || ref.verticalCollision;
    }

    public static boolean isCollidingHorizontally(Entity ref) {
        return ref.horizontalCollision;
    }

    public static boolean isCollidingVertically(Entity ref) {
        return ref.verticalCollision;
    }

    public static StatusEffectInstance getEffect(Entity ref, StatusEffect effect) {
        var statusEffect = Registries.STATUS_EFFECT.getEntry(effect);
        if (!(ref instanceof LivingEntity liv))
            return new StatusEffectInstance(statusEffect);
        StatusEffectInstance effectInstance = liv.getStatusEffect(statusEffect);
        return effectInstance != null ? effectInstance : new StatusEffectInstance(statusEffect);
    }

    public static boolean isHolding(Entity ref, Predicate<ItemStack> item) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        ItemStack stack = liv.getStackInHand(Hand.MAIN_HAND);
        return stack != null && item.test(stack);
    }

    public static boolean isOffHolding(Entity ref, Predicate<ItemStack> item) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        ItemStack stack = liv.getStackInHand(Hand.OFF_HAND);
        return stack != null && item.test(stack);
    }

    public static List<ItemStack> getArmorItems(LivingEntity ent) {
        List<ItemStack> items = new ArrayList<>();
        items.add(ent.getEquippedStack(EquipmentSlot.HEAD));
        items.add(ent.getEquippedStack(EquipmentSlot.CHEST));
        items.add(ent.getEquippedStack(EquipmentSlot.LEGS));
        items.add(ent.getEquippedStack(EquipmentSlot.FEET));
        return items;
    }

    public static List<ItemStack> getHandItems(LivingEntity ent) {
        List<ItemStack> items = new ArrayList<>();
        items.add(ent.getEquippedStack(EquipmentSlot.MAINHAND));
        items.add(ent.getEquippedStack(EquipmentSlot.OFFHAND));
        return items;
    }

    public static boolean hasEquipment(Entity ref, Predicate<ItemStack> item) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        for (ItemStack armor : getArmorItems(liv))
            if (item.test(armor))
                return true;
        return false;
    }

    public static Entity getRenderStateOwner(EntityRenderState state) {
        if (PlayerUtils.invalid())
            return null;
        for (Entity entity: PlayerUtils.getClientWorld().getEntities())
            if (state.squaredDistanceToCamera == entity.squaredDistanceTo(mc.gameRenderer.getCamera().getCameraPos()))
                return entity;
        return null;
    }

    public static HitResult getTarget(Entity ref) {
        if (ref == PlayerUtils.player())
            return mc.crosshairTarget;

        double rangeB = 5.0;
        double rangeE = 3.0;
        double d = rangeB;
        double e = MathHelper.square(d);
        Vec3d vec3d = ref.getCameraPosVec(1.0F);
        HitResult hitResult = ref.raycast(d, 1.0F, false);
        double f = hitResult.getPos().squaredDistanceTo(vec3d);

        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        Vec3d rot = ref.getRotationVec(1.0F);
        Vec3d vec = vec3d.add(rot.x * d, rot.y * d, rot.z * d);
        Box box = ref.getBoundingBox().stretch(rot.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entHit = ProjectileUtil.raycast(ref, vec3d, vec, box, ent -> !ent.isSpectator() && ent.canHit(), e);
        return entHit != null && entHit.getPos().squaredDistanceTo(vec3d) < f ? ensureTargetInRange(entHit, vec3d, rangeE) : ensureTargetInRange(hitResult, vec3d, rangeB);
    }

    public static Voidable<FluidState> getTargetFluid(Entity ref, boolean prioritizeSource) {
        World world = ref.getEntityWorld();
        Vec3d eye = ref.getEyePos();
        Vec3d dir = ref.getRotationVector().normalize();
        FluidState targetState = null;

        for (double dist = 0; dist < 5; dist += 0.1) {
            Vec3d point = eye.add(dir.multiply(dist));
            BlockPos pos = BlockPos.ofFloored(point);
            FluidState state = world.getFluidState(pos);

            if (state.isEmpty())
                continue;

            if (targetState == null) {
                targetState = state;
                if (!prioritizeSource)
                    break;
            }

            if (state.getLevel() == 8) {
                targetState = state;
                break;
            }
        }
        return Voidable.of(targetState);
    }

    private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d cameraPos, double interactionRange) {
        Vec3d vec = hitResult.getPos();
        if (!vec.isInRange(cameraPos, interactionRange)) {
            Vec3d hit = hitResult.getPos();
            Direction direction = Direction.getFacing(hit.x - cameraPos.x, hit.y - cameraPos.y, hit.z - cameraPos.z);
            return BlockHitResult.createMissed(hit, direction, BlockPos.ofFloored(hit));
        }
        return hitResult;
    }

    public static Entity getNearestEntity(Entity ref, double range, Predicate<Entity> filter) {
        Vec3d at = ref.getEntityPos();
        List<Entity> candidates = ref.getEntityWorld()
                .getOtherEntities(PlayerUtils.player(), Box.from(at).expand(range), ent -> ent != ref && filter.test(ent)).stream()
                .sorted(Comparator.comparing(entity -> entity.getEntityPos().distanceTo(at)))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(0);
    }

    public static void runOnNearestBlock(Entity ref, double range, BiPredicate<BlockPos, BlockState> filter, BiConsumer<BlockPos, BlockState> function) {
        if (PlayerUtils.invalid()) {
            return;
        }

        AtomicReference<Double> nearestDist = new AtomicReference<>(64.0);
        AtomicReference<BlockPos> nearestPos = new AtomicReference<>();
        AtomicReference<BlockState> nearestState = new AtomicReference<>();
        Box box = ref.getBoundingBox().expand(range);
        Vec3d player = ref.getEntityPos();
        World world = ref.getEntityWorld();

        PlayerUtils.boxIterator(world, box, (pos, state) -> {
            if (filter.test(pos, state) && pos.isWithinDistance(player, nearestDist.get())) {
                nearestDist.set(Math.sqrt(pos.getSquaredDistance(player)));
                nearestPos.set(pos);
                nearestState.set(state);
            }
        });

        if (nearestState.get() != null && nearestPos.get() != null) {
            function.accept(nearestPos.get(), nearestState.get());
        }
    }

    public static void runOnNearestBlock(Entity ref, double range, Predicate<BlockState> filter, BiConsumer<BlockPos, BlockState> function) {
        runOnNearestBlock(ref, range, (pos, state) -> filter.test(state), function);
    }

    public static void runOnNearestEntity(Entity ref, double range, Predicate<Entity> filter, Consumer<Entity> function) {
        if (PlayerUtils.invalid())
            return;

        Entity ent = getNearestEntity(ref, range, filter);

        if (ent != null)
            function.accept(ent);
    }

    public static boolean isTeammate(PlayerEntity target) {
        TeamDetector teamDetector = Module.get(TeamDetector.class);
        if (!teamDetector.isEnabled())
            return false;

        // Check manual list first
        String[] names = teamDetector.playerNames.getVal().split(",");
        String targetName = target.getName().getString();
        for (String name : names) {
            if (name.trim().equalsIgnoreCase(targetName)) {
                return true;
            }
        }

        // Check automatic detection
        if (teamDetector.teamFindingMethod.getVal() == TeamDetector.TeamsMethod.SCOREBOARD)
            return isSameScoreboardTeam(target);
        else if (teamDetector.teamFindingMethod.getVal() == TeamDetector.TeamsMethod.COLOR_NAME)
            return isSameColorNameTeam(target);
        return false;
    }

    public static boolean shouldCancelCcsAttack(PlayerEntity target) {
        TeamDetector teamDetector = Module.get(TeamDetector.class);
        return teamDetector.isEnabled() && teamDetector.cancelCcs.getVal() && isTeammate(target);
    }
  
    public static boolean isSameScoreboardTeam(PlayerEntity player) {
        Scoreboard scoreboard = PlayerUtils.getWorld().getScoreboard();
        Team playerTeam = scoreboard.getTeam(PlayerUtils.player().getName().getString());
        Team otherPlayerTeam = scoreboard.getTeam(player.getName().getString());
        return playerTeam != null && playerTeam.equals(otherPlayerTeam);
    }

    public static boolean isSameColorNameTeam(PlayerEntity player) {
        int playerColor = PlayerUtils.player().getTeamColorValue();
        int targetColor = player.getTeamColorValue();
        return playerColor == targetColor && playerColor != Formatting.WHITE.getCode();
    }

    public static List<Entity> getEntitiesAt(BlockPos pos) {
        if (PlayerUtils.invalid())
            return new ArrayList<>();

        List<Entity> list = new ArrayList<>();

        for (Entity ent : PlayerUtils.getClientWorld().getEntities())
            if (ent != null && ent.isAlive() && pos.equals(ent.getBlockPos()))
                list.add(ent);
        return list;
    }

    public static boolean checkEntityAt(BlockPos pos, Predicate<Entity> filter) {
        return getEntitiesAt(pos).stream().anyMatch(filter);
    }
}
