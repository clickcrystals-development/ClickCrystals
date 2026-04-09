package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.TeamDetector;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

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
        return mc.crosshairPickEntity != null;
    }

    public static boolean isMoving(Entity ref) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        return liv.xxa != 0 || liv.zza != 0;
    }

    public static boolean isBlocking(Entity ref) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        if (!liv.isBlocking())
            return false;
            
        // Check if shield is facing the attacker
        if (PlayerUtils.invalid())
            return true;
            
        Vec3 targetLook = liv.getLookAngle().normalize();
        Vec3 toAttacker = PlayerUtils.player().position().subtract(liv.position()).normalize();
        double dot = targetLook.dot(toAttacker);
        
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

    public static MobEffectInstance getEffect(Entity ref, MobEffect effect) {
        var statusEffect = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
        if (!(ref instanceof LivingEntity liv))
            return new MobEffectInstance(statusEffect);
        MobEffectInstance effectInstance = liv.getEffect(statusEffect);
        return effectInstance != null ? effectInstance : new MobEffectInstance(statusEffect);
    }

    public static boolean isHolding(Entity ref, Predicate<ItemStack> item) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        ItemStack stack = liv.getItemInHand(InteractionHand.MAIN_HAND);
        return stack != null && item.test(stack);
    }

    public static boolean isOffHolding(Entity ref, Predicate<ItemStack> item) {
        if (!(ref instanceof LivingEntity liv))
            return false;
        ItemStack stack = liv.getItemInHand(InteractionHand.OFF_HAND);
        return stack != null && item.test(stack);
    }

    public static List<ItemStack> getArmorItems(LivingEntity ent) {
        List<ItemStack> items = new ArrayList<>();
        items.add(ent.getItemBySlot(EquipmentSlot.HEAD));
        items.add(ent.getItemBySlot(EquipmentSlot.CHEST));
        items.add(ent.getItemBySlot(EquipmentSlot.LEGS));
        items.add(ent.getItemBySlot(EquipmentSlot.FEET));
        return items;
    }

    public static List<ItemStack> getHandItems(LivingEntity ent) {
        List<ItemStack> items = new ArrayList<>();
        items.add(ent.getItemBySlot(EquipmentSlot.MAINHAND));
        items.add(ent.getItemBySlot(EquipmentSlot.OFFHAND));
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
        for (Entity entity: PlayerUtils.getClientWorld().entitiesForRendering())
            if (state.distanceToCameraSq == entity.distanceToSqr(mc.gameRenderer.getMainCamera().position()))
                return entity;
        return null;
    }

    public static HitResult getTarget(Entity ref) {
        if (ref == PlayerUtils.player())
            return mc.hitResult;

        double rangeB = 5.0;
        double rangeE = 3.0;
        double d = rangeB;
        double e = Mth.square(d);
        Vec3 vec3d = ref.getEyePosition(1.0F);
        HitResult hitResult = ref.pick(d, 1.0F, false);
        double f = hitResult.getLocation().distanceToSqr(vec3d);

        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        Vec3 rot = ref.getViewVector(1.0F);
        Vec3 vec = vec3d.add(rot.x * d, rot.y * d, rot.z * d);
        AABB box = ref.getBoundingBox().expandTowards(rot.scale(d)).inflate(1.0, 1.0, 1.0);
        EntityHitResult entHit = ProjectileUtil.getEntityHitResult(ref, vec3d, vec, box, ent -> !ent.isSpectator() && ent.isPickable(), e);
        return entHit != null && entHit.getLocation().distanceToSqr(vec3d) < f ? ensureTargetInRange(entHit, vec3d, rangeE) : ensureTargetInRange(hitResult, vec3d, rangeB);
    }

    public static Voidable<FluidState> getTargetFluid(Entity ref, boolean prioritizeSource) {
        Level world = ref.level();
        Vec3 eye = ref.getEyePosition();
        Vec3 dir = ref.getLookAngle().normalize();
        FluidState targetState = null;

        for (double dist = 0; dist < 5; dist += 0.1) {
            Vec3 point = eye.add(dir.scale(dist));
            BlockPos pos = BlockPos.containing(point);
            FluidState state = world.getFluidState(pos);

            if (state.isEmpty())
                continue;

            if (targetState == null) {
                targetState = state;
                if (!prioritizeSource)
                    break;
            }

            if (state.getAmount() == 8) {
                targetState = state;
                break;
            }
        }
        return Voidable.of(targetState);
    }

    private static HitResult ensureTargetInRange(HitResult hitResult, Vec3 cameraPos, double interactionRange) {
        Vec3 vec = hitResult.getLocation();
        if (!vec.closerThan(cameraPos, interactionRange)) {
            Vec3 hit = hitResult.getLocation();
            Direction direction = Direction.getApproximateNearest(hit.x - cameraPos.x, hit.y - cameraPos.y, hit.z - cameraPos.z);
            return BlockHitResult.miss(hit, direction, BlockPos.containing(hit));
        }
        return hitResult;
    }

    public static Entity getNearestEntity(Entity ref, double range, Predicate<Entity> filter) {
        Vec3 at = ref.position();
        List<Entity> candidates = ref.level()
                .getEntities(PlayerUtils.player(), AABB.unitCubeFromLowerCorner(at).inflate(range), ent -> ent != ref && filter.test(ent)).stream()
                .sorted(Comparator.comparing(entity -> entity.position().distanceTo(at)))
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
        AABB box = ref.getBoundingBox().inflate(range);
        Vec3 player = ref.position();
        Level world = ref.level();

        PlayerUtils.boxIterator(world, box, (pos, state) -> {
            if (filter.test(pos, state) && pos.closerToCenterThan(player, nearestDist.get())) {
                nearestDist.set(Math.sqrt(pos.distToCenterSqr(player)));
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

    public static boolean isTeammate(Player target) {
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

    public static boolean shouldCancelCcsAttack(Player target) {
        TeamDetector teamDetector = Module.get(TeamDetector.class);
        return teamDetector.isEnabled() && teamDetector.cancelCcs.getVal() && isTeammate(target);
    }
  
    public static boolean isSameScoreboardTeam(Player player) {
        Scoreboard scoreboard = PlayerUtils.getWorld().getScoreboard();
        PlayerTeam playerTeam = scoreboard.getPlayerTeam(PlayerUtils.player().getName().getString());
        PlayerTeam otherPlayerTeam = scoreboard.getPlayerTeam(player.getName().getString());
        return playerTeam != null && playerTeam.equals(otherPlayerTeam);
    }

    public static boolean isSameColorNameTeam(Player player) {
        int playerColor = PlayerUtils.player().getTeamColor();
        int targetColor = player.getTeamColor();
        return playerColor == targetColor && playerColor != ChatFormatting.WHITE.getChar();
    }

    public static List<Entity> getEntitiesAt(BlockPos pos) {
        if (PlayerUtils.invalid())
            return new ArrayList<>();

        List<Entity> list = new ArrayList<>();

        for (Entity ent : PlayerUtils.getClientWorld().entitiesForRendering())
            if (ent != null && ent.isAlive() && pos.equals(ent.blockPosition()))
                list.add(ent);
        return list;
    }

    public static boolean checkEntityAt(BlockPos pos, Predicate<Entity> filter) {
        return getEntitiesAt(pos).stream().anyMatch(filter);
    }
}
