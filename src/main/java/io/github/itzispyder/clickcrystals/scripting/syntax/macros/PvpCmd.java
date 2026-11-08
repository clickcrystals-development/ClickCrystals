package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Main-thread, bounded PvP primitives for custom CCS modules.
 *
 * Commands:
 *   pvp lunge_swap   - equip a spear and attack in the SAME client tick;
 *                      restore the original hotbar slot the next tick.
 *   pvp anchor_dual  - one guarded place -> single charge -> single use cycle.
 *   pvp reset        - cancel this script's outstanding PvP tasks.
 *
 * No hold_input, simulated held buttons, wall-clock waits, or attack spamming.
 * Attribute swap is a vanilla timing quirk; server plugins may reject it.
 */
public final class PvpCmd extends ScriptCommand implements Global {
    private static final String SWAP_BUSY = "pvp_swap_busy";
    private static final String SWAP_ORIG = "pvp_swap_original";
    private static final String SWAP_EQUIP = "pvp_swap_equipped";
    private static final String ANCHOR_BUSY = "pvp_anchor_busy";
    private static final String ANCHOR_ORIG = "pvp_anchor_original";
    private static final String ANCHOR_EQUIP = "pvp_anchor_equipped";

    public PvpCmd() {
        super("pvp");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() != 1)
            throw new IllegalArgumentException("Usage: pvp lunge_swap|anchor_dual|reset");
        ClickScript script = args.getExecutorOrDef();
        switch (args.get(0).toString().toLowerCase(java.util.Locale.ROOT)) {
            case "lunge_swap" -> lungeSwap(script);
            case "anchor_dual" -> anchorDual(script);
            case "reset" -> reset(script);
            default -> throw new IllegalArgumentException("Usage: pvp lunge_swap|anchor_dual|reset");
        }
    }

    private static boolean ready() {
        return !PlayerUtils.invalid() && mc.world != null && mc.currentScreen == null;
    }

    private static boolean active(ClickScript script, String key) {
        return "true".equals(script.getScriptState().get(key));
    }

    private static void remember(ClickScript script, String busy, String oldSlot, String equipped,
                                 int original, int newSlot) {
        script.getScriptState().set(busy, "true");
        script.getScriptState().set(oldSlot, Integer.toString(original));
        script.getScriptState().set(equipped, Integer.toString(newSlot));
    }

    private static void updateEquipped(ClickScript script, String key, int slot) {
        script.getScriptState().set(key, Integer.toString(slot));
    }

    private static void restoreIfUntouched(ClickScript script, String oldKey, String equippedKey) {
        if (!ready()) return;
        try {
            int original = Integer.parseInt(script.getScriptState().get(oldKey));
            int equipped = Integer.parseInt(script.getScriptState().get(equippedKey));
            if (original >= 0 && original < 9 && InvUtils.selected() == equipped)
                InvUtils.select(original);
        } catch (NumberFormatException ignored) {
            // No saved selection: never overwrite the player's manually chosen slot.
        }
    }

    private static void endSwap(ClickScript script) {
        restoreIfUntouched(script, SWAP_ORIG, SWAP_EQUIP);
        script.getScriptState().clear(SWAP_BUSY);
        script.getScriptState().clear(SWAP_ORIG);
        script.getScriptState().clear(SWAP_EQUIP);
    }

    private static void lungeSwap(ClickScript script) {
        if (!ready() || active(script, SWAP_BUSY)) return;
        if (mc.player.getHungerManager().getFoodLevel() < 6 ||
                mc.player.isTouchingWater() || mc.player.isGliding()) return;
        // The cooldown checked here is for the weapon held BEFORE the swap.
        if (mc.player.getAttackCooldownProgress(0.0F) < 0.99F) return;

        int original = InvUtils.selected();
        int spear = HotbarUtils.searchSlot(stack -> !stack.isEmpty()
                && Registries.ITEM.getId(stack.getItem()).getPath().endsWith("_spear"));
        if (spear < 0 || spear == original) return;

        remember(script, SWAP_BUSY, SWAP_ORIG, SWAP_EQUIP, original, spear);
        try {
            // This precise order is intentional. No wait between select and attack.
            InvUtils.select(spear);
            InteractionUtils.inputAttack();
            script.scheduleCallbackTask("pvp_swap_restore", 1, 0, () -> endSwap(script));
        } catch (RuntimeException ex) {
            endSwap(script);
            throw ex;
        }
    }

    private static int findSlot(net.minecraft.item.Item item) {
        return HotbarUtils.searchSlot(s -> !s.isEmpty() && s.isOf(item));
    }

    private static boolean pointingAt(BlockPos expected) {
        return mc.crosshairTarget instanceof BlockHitResult hit &&
                hit.getBlockPos().equals(expected);
    }

    private static int charge(BlockPos pos) {
        if (!ready() || !mc.world.getBlockState(pos).isOf(Blocks.RESPAWN_ANCHOR))
            return -1;
        BlockState state = mc.world.getBlockState(pos);
        return state.get(RespawnAnchorBlock.CHARGES);
    }

    private static void endAnchor(ClickScript script) {
        restoreIfUntouched(script, ANCHOR_ORIG, ANCHOR_EQUIP);
        script.getScriptState().clear(ANCHOR_BUSY);
        script.getScriptState().clear(ANCHOR_ORIG);
        script.getScriptState().clear(ANCHOR_EQUIP);
    }

    private static void anchorDual(ClickScript script) {
        if (!ready() || active(script, ANCHOR_BUSY)) return;
        // Nether anchors are respawn points, not explosives. Fail closed.
        if (mc.world.getRegistryKey().equals(World.NETHER)) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) return;

        int anchors = findSlot(Items.RESPAWN_ANCHOR);
        int glowstone = findSlot(Items.GLOWSTONE);
        if (anchors < 0 || glowstone < 0) return;

        BlockPos target;
        int old = InvUtils.selected();
        BlockState aimed = mc.world.getBlockState(hit.getBlockPos());
        boolean alreadyAnchor = aimed.isOf(Blocks.RESPAWN_ANCHOR);
        if (alreadyAnchor) {
            target = hit.getBlockPos();
            if (charge(target) > 1) return; // never overcharge / blindly trigger other anchors
        } else {
            target = hit.getBlockPos().offset(hit.getSide());
            if (!mc.world.getBlockState(target).isReplaceable()) return;
        }

        remember(script, ANCHOR_BUSY, ANCHOR_ORIG, ANCHOR_EQUIP, old, alreadyAnchor ? old : anchors);
        try {
            if (!alreadyAnchor) {
                InvUtils.select(anchors);
                // One place attempt, not a held right-click.
                InteractionUtils.inputUse();
            }
            script.scheduleCallbackTask("pvp_anchor_charge", 1, 0, () ->
                    chargeStep(script, target, glowstone));
        } catch (RuntimeException ex) {
            endAnchor(script);
            throw ex;
        }
    }

    private static void chargeStep(ClickScript script, BlockPos target, int glowstone) {
        try {
            if (!ready() || !pointingAt(target)) { endAnchor(script); return; }
            int current = charge(target);
            if (current < 0 || current > 1) { endAnchor(script); return; }
            if (current == 0) {
                if (!InvUtils.inv().getStack(glowstone).isOf(Items.GLOWSTONE)) {
                    endAnchor(script); return;
                }
                InvUtils.select(glowstone);
                updateEquipped(script, ANCHOR_EQUIP, glowstone);
                InteractionUtils.inputUse(); // exactly one charge attempt
            }
            script.scheduleCallbackTask("pvp_anchor_detonate", 1, 0, () ->
                    detonateStep(script, target));
        } catch (RuntimeException ex) {
            endAnchor(script);
            throw ex;
        }
    }

    private static int safeDetonationSlot(int preferred) {
        ItemStack first = InvUtils.inv().getStack(preferred);
        if (!first.isOf(Items.GLOWSTONE) && !first.isOf(Items.RESPAWN_ANCHOR))
            return preferred;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = InvUtils.inv().getStack(i);
            if (!stack.isOf(Items.GLOWSTONE) && !stack.isOf(Items.RESPAWN_ANCHOR))
                return i;
        }
        return -1;
    }

    private static void detonateStep(ClickScript script, BlockPos target) {
        try {
            if (!ready() || !pointingAt(target) || charge(target) != 1) {
                endAnchor(script); return;
            }
            int original = Integer.parseInt(script.getScriptState().get(ANCHOR_ORIG));
            int safe = safeDetonationSlot(original);
            if (safe < 0) { endAnchor(script); return; }
            InvUtils.select(safe);
            updateEquipped(script, ANCHOR_EQUIP, safe);
            InteractionUtils.inputUse(); // exactly one activation attempt
            script.scheduleCallbackTask("pvp_anchor_restore", 1, 0, () -> endAnchor(script));
        } catch (RuntimeException ex) {
            endAnchor(script);
            throw ex;
        }
    }

    private static void reset(ClickScript script) {
        script.cancelTask("pvp_swap_restore");
        script.cancelTask("pvp_anchor_charge");
        script.cancelTask("pvp_anchor_detonate");
        script.cancelTask("pvp_anchor_restore");
        endSwap(script);
        endAnchor(script);
    }
}
