# PvP Runtime Upgrade — 1.21.11

Maintainer: **Gergherobrine**. Source branch: `fix/pvp-runtime-2026-11-08`.

**Status: experimental, intended for controlled Minecraft 1.21.11 testing.**
This is not a fully tested PvP/anticheat solution. It does not guarantee execution
across every server, Minecraft version, network latency, or mod conflict.

## Core findings

- The previous SpearSwap `restore` token was not a registered CCS command.
- Java 1.21.11 spears have a forced jab cooldown. Attribute/lunge swapping is
  a vanilla item-switch + attack timing quirk, not "wait for spear cooldown".
- Use the old/current weapon's charged cooldown, select a Lunge spear, issue
  exactly one attack in the same client tick, and return the original slot next tick.
- The old Anchor Dual used timed callbacks/held-input patterns. A delayed held
  use key could stay pressed after release. `TickEventListener.cancelTickInputs`
  now releases injected input flags immediately.
- `Custom Made` previously hid sources without a registered ScriptedModule.
  Unloaded files are now visible with an `[NOT LOADED]` label and open in the IDE.
- The previous GUI dynamically displayed upstream owners as if they maintained
  Modified. The UI now displays Gergherobrine as the fork's developer.
  Original MIT authorship notices remain legally intact.

## New CCS command

`pvp lunge_swap`
- One action per CCS invocation. Requires hotbar spear, >= 6 hunger, not in
  water/elytra, main weapon cooldown near 100%, and no open GUI.
- Selects spear and calls inputAttack in the same client tick, with no fixed
  spear cooldown wait. Restores only if the user hasn't manually selected
  another hotbar slot.
- No automatic target acquisition. It uses the player's actual crosshair.
- The client-side sequence can be confirmed by instrumentation, but its effect
  on a given server must still be tested.

`pvp anchor_dual`
- One bounded sequence per invocation: place anchor (when looking at a valid
  block face), wait to observe anchor, charge once, wait to observe charge 1,
  activate once, restore if the hotbar selection is unchanged.
- If already looking at a charged-one anchor, it uses it once; anchors with
  more than one charge are never deliberately detonated.
- Requires anchor/glowstone in the hotbar and a non-glowstone, non-anchor
  detonation item. Never executes in the Nether.
- Cannot place glowstone as a fallback when the anchor has not appeared or
  the player has aimed away. Waits at most 8 ticks for each state update.
- This cannot guarantee safety from the explosion: vanilla splash damage is
  server-authoritative. Test on a private world.

`pvp reset`
- Cancels this script's PvP continuations, clears busy states, restores
  previously recorded slot only if it has not been manually changed.

The two sample modules are in `examples/pvp`:
- `01_Gergherobrine_AnchorDual_v3.ccs` — B
- `02_Gergherobrine_SpearSwap_v3.ccs` — G

Place ONLY the .ccs files, not the entire project, in
`.minecraft/.clickcrystals/scripts/`. The new `pvp` command requires
the newly compiled Modified JAR from this branch, not old dev.5 releases.

## Additional read-only CCS sensors

- target_velocity_x, target_velocity_y, target_velocity_z
- target_speed, target_speed_horizontal
- target_sprinting, target_grounded, target_entity_type

These sensors describe the **entity under the crosshair** and return no value
when there is no such target. They are a foundation for movement prediction,
not an autonomous target-tracking or AI system.

## PvP domain map for future development

- Sword/Axe: attack cooldown, sprint reset, spacing, shield timings, swaps.
- Crystal: placement, explosion exposure, totem swaps, hole/flat fighting.
- Anchor: placement/charge/use state, explosion safety, item ownership.
- Mace: vertical movement, wind charges, critical hit and stun-slam setups.
- Spear: jab/charge separation, range, Lunge, attribute swapping, DTap.
- Elytra: speed, glide/mace transitions, inventory interaction.
- SMP/Hybrid: potions, apples, totems, all-inventory resource scheduling.

Order of future implementation: stable lifecycle and input arbitration;
packet/slot change diagnostics; target motion snapshots; finite-state combo
graphs with rollback; world-coordinate safety checks; unit/client simulation;
cross-version Fabric adapters.

## Regression checklist

1. Compile Java 21 / Fabric 1.21.11 with GitHub Actions.
2. Remove duplicate ClickCrystals JARs from mods.
3. Remove previous Anchor Dual and Spear Swap sources from scripts folder.
4. Copy the two new samples into scripts folder, restart or reload scripts.
5. Confirm both register in Custom Made. Deliberately put a bad CCS file in
   the folder: it must appear [NOT LOADED] and open in the IDE.
6. Anchor B held for 2 seconds: exactly one sequence, never persistent use.
7. Anchor B pressed quickly: at most one anchor placement and one charge,
   then at most one activation; abort if crosshair moves or anchor state fails.
8. Spear G with charged main weapon and Lunge spear: select+attack same tick,
   original item restored next tick if the player has not changed their slot.
9. Spear G with hunger <6, GUI open, or no spear: no automated action.
10. Disable module mid-sequence, leave world, reload scripts: no repeat tasks
    or forever-held attack/use keys.
11. Repeat with high ping; check that 8-tick state limits abort safely.
12. Validate on every target Minecraft version separately: this branch only
    implements 1.21.11/Yarn and does not certify 26.1.2 or 26.2.

## Notes on credits

The user-facing owner panel is specific to Gergherobrine's Modified fork.
This does not remove upstream authorship, MIT LICENSE notices, or headers.

## References

- Official Minecraft Java Edition 1.21.11 release notes:
  https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-11
- Minecraft Wiki attribute swapping:
  https://minecraft.wiki/w/Tutorial:Attribute_swapping
- Minecraft Wiki modern Java PvP:
  https://minecraft.wiki/w/Tutorial:PvP_(Java_Edition)
- Minecraft issue MC-28289 (timing / previous-item attributes).
