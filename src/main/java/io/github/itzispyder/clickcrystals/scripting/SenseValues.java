package io.github.itzispyder.clickcrystals.scripting;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import java.util.Locale;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Read-only client-side combat and gameplay data for user CCS scripts.
 * Never changes inventory, aim, motion or network state.
 * Values are local snapshots; no promise is made about server confirmation.
 */
public final class SenseValues implements Global {
    private SenseValues() {}

    private static String bool(boolean value) { return Boolean.toString(value); }
    private static String number(double value) {
        if (!Double.isFinite(value)) return "";
        return Double.toString(value);
    }

    private static ItemStack stack(int index) { return mc.player.getInventory().getItem(index); }
    private static String itemId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "minecraft:air";
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }
    public static String slotItem(int oneBasedSlot) {
        if (oneBasedSlot < 1 || oneBasedSlot > 9)
            throw new IllegalArgumentException("Hotbar slot must be 1..9");
        if (PlayerUtils.invalid()) return null;
        return itemId(stack(oneBasedSlot - 1));
    }

    /** Number of individual items in hotbar stacks, not number of slots. */
    public static int countItem(String id) {
        if (id == null || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Use item ID such as minecraft:totem_of_undying");
        if (PlayerUtils.invalid()) return -1;
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = stack(i);
            if (id.equals(itemId(stack))) sum += stack.getCount();
        }
        return sum;
    }

    private static int emptySlots() {
        int count = 0;
        for (int i = 0; i < 9; i++) if (stack(i).isEmpty()) count++;
        return count;
    }

    private static boolean has(String id) { return countItem(id) > 0; }
    private static boolean kitCrystal() {
        return has("minecraft:end_crystal") && has("minecraft:obsidian") &&
                (has("minecraft:totem_of_undying") ||
                 itemId(mc.player.getOffhandItem()).equals("minecraft:totem_of_undying"));
    }
    private static boolean kitAnchor() {
        return has("minecraft:respawn_anchor") && has("minecraft:glowstone") &&
                (has("minecraft:totem_of_undying") ||
                 itemId(mc.player.getOffhandItem()).equals("minecraft:totem_of_undying"));
    }
    private static boolean kitMace() {
        return has("minecraft:mace") &&
                (has("minecraft:wind_charge") || has("minecraft:ender_pearl"));
    }
    private static boolean kitCart() {
        return has("minecraft:rail") && has("minecraft:tnt_minecart") &&
                (has("minecraft:bow") || has("minecraft:crossbow"));
    }
    private static String blockId() {
        if (!(mc.hitResult instanceof BlockHitResult hit)) return null;
        return BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(hit.getBlockPos()).getBlock()).toString();
    }
    private static String anchorCharge() {
        if (!(mc.hitResult instanceof BlockHitResult hit)) return null;
        if (!(mc.level.getBlockState(hit.getBlockPos()).is(Blocks.RESPAWN_ANCHOR))) return null;
        return Integer.toString(mc.level.getBlockState(hit.getBlockPos()).getValue(RespawnAnchorBlock.CHARGE));
    }
    private static String handDurability(ItemStack stack) {
        if (stack.getMaxDamage() <= 0) return "-1";
        return Integer.toString(stack.getMaxDamage() - stack.getDamageValue());
    }

    private static String dimensionId() {
        String key = mc.level.dimension().toString();
        int slash = key.lastIndexOf('/');
        if (slash < 0) return key;
        int bracket = key.indexOf(']', slash);
        return key.substring(slash + 1, bracket < 0 ? key.length() : bracket).trim();
    }

    /** Returns null for unavailable readings, never stale state. */
    public static String read(String name) {
        if (name == null) throw new IllegalArgumentException("Missing sensor name");
        if (PlayerUtils.invalid()) return null;
        var p = mc.player;
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "health" -> number(p.getHealth());
            case "max_health" -> number(p.getMaxHealth());
            case "health_pct" -> number(p.getHealth() * 100.0 / Math.max(1.0, p.getMaxHealth()));
            case "absorption" -> number(p.getAbsorptionAmount());
            case "food", "hunger" -> Integer.toString(p.getFoodData().getFoodLevel());
            case "saturation" -> number(p.getFoodData().getSaturationLevel());
            case "armor" -> Integer.toString(p.getArmorValue());
            case "air" -> Integer.toString(p.getAirSupply());
            case "xp_level" -> Integer.toString(p.experienceLevel);
            case "fall_distance" -> number(p.fallDistance);
            case "velocity_y" -> number(p.getDeltaMovement().y);
            case "attack_cooldown" -> number(p.getAttackStrengthScale(0.0F));
            case "crit_candidate" -> bool(!(p.onGround()) && p.fallDistance > 0.0F && (p.getDeltaMovement().y < -0.01) && (p.getAttackStrengthScale(0.0F)) >= 0.848F);
            case "grounded" -> bool(p.onGround());
            case "falling" -> bool(p.getDeltaMovement().y < -0.01);
            case "moving" -> bool(PlayerUtils.isMoving());
            case "sprinting" -> bool(p.isSprinting());
            case "sneaking" -> bool(p.isShiftKeyDown());
            case "swimming" -> bool(p.isSwimming());
            case "gliding" -> bool(p.isFallFlying());
            case "using_item" -> bool(p.isUsingItem());
            case "in_water" -> bool(p.isInWater());
            case "on_fire" -> bool(p.isOnFire());
            case "selected_slot" -> Integer.toString(InvUtils.selected() + 1);
            case "mainhand_item" -> itemId(p.getMainHandItem());
            case "offhand_item" -> itemId(mc.player.getOffhandItem());
            case "mainhand_count" -> Integer.toString(p.getMainHandItem().getCount());
            case "offhand_count" -> Integer.toString(p.getOffhandItem().getCount());
            case "mainhand_durability" -> handDurability(p.getMainHandItem());
            case "offhand_durability" -> handDurability(p.getOffhandItem());
            case "hotbar_empty_slots" -> Integer.toString(emptySlots());
            case "totems_hotbar" -> Integer.toString(countItem("minecraft:totem_of_undying"));
            case "crystals_hotbar" -> Integer.toString(countItem("minecraft:end_crystal"));
            case "obsidian_hotbar" -> Integer.toString(countItem("minecraft:obsidian"));
            case "anchors_hotbar" -> Integer.toString(countItem("minecraft:respawn_anchor"));
            case "glowstone_hotbar" -> Integer.toString(countItem("minecraft:glowstone"));
            case "wind_charges_hotbar" -> Integer.toString(countItem("minecraft:wind_charge"));
            case "pearls_hotbar" -> Integer.toString(countItem("minecraft:ender_pearl"));
            case "kit_crystal" -> bool(kitCrystal());
            case "kit_anchor" -> bool(kitAnchor());
            case "kit_mace" -> bool(kitMace());
            case "kit_cart" -> bool(kitCart());
            case "fps" -> Integer.toString(PlayerUtils.getFps());
            case "ping" -> Long.toString(PlayerUtils.getPing());
            case "dimension" -> dimensionId();
            case "x" -> number(p.getX());
            case "y" -> number(p.getY());
            case "z" -> number(p.getZ());
            case "target_kind" -> mc.hitResult instanceof BlockHitResult ? "block" : mc.hitResult instanceof EntityHitResult ? "entity" : "miss";
            case "target_block" -> blockId();
            case "target_anchor_charge" -> anchorCharge();
            case "target_distance" -> mc.hitResult == null || mc.hitResult.getType() == HitResult.Type.MISS ? null : number(p.getEyePosition().distanceTo(mc.hitResult.getLocation()));
            default -> throw new IllegalArgumentException("Unsupported sense metric: " + name);
        };
    }
}
