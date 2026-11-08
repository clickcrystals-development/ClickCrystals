# OMNIKIT Runtime Foundation — development audit

Status: **incremental infrastructure improvements**, NOT a completed OmniKit Combat Engine.

## Baseline

- Repository: `Gergherobrine/ClickCrystals-Modified`
- Primary platform: Minecraft Java 1.21.11, Fabric / Java 21.
- Base commit: `c4b56f381c40236fb640091abf4d64501cbcc0c0`.
- Base source branch: `fix/ccs-comments-12111-dev7`.
- Base version: `1.21.11-1.4.5-dev.7`.
- Development branch: `feat/omnikit-runtime-foundation-12111`.
- No changes to `v1.4.4` or release tags.

The long-term aim is a stable CCS platform for user-authored combat, inventory, utility and navigation scripts. This branch does **not** publish personal CCS modules.

## Confirmed audit findings and changes

| Area | Source finding | Change | Evidence |
| --- | --- | --- | --- |
| CCS execution | Old dispatch rejected nesting above 32; there was no common command count limit | Added `ScriptExecutionBudget`: 64 nested frames and 4096 dispatched commands per outer event | `ScriptExecutionBudgetTest`; CCS integration in `ClickScript` |
| Async computation | A worker removed its pending entry before the queued main-thread callback ran; `cancel(id)` could no longer stop that callback | Keep the entry pending until callback delivery, and revalidate cancellation at delivery | `AsyncDecisionBridgeTest` |
| Simulated input | Old delayed release could clear a newer hold after cancel/reacquire | Added generation tickets for the eight simulated actions and keyed holds | `InputLeaseManagerTest` |
| Timed tasks | Failures were printed only to standard error | Task failures now report through the owning script's error reporting hook | `TickTaskQueueTest` |
| Inventory packets | `InvUtils.sendSlotPacket` hand-built packets with fixed sync ID and revision | Use Minecraft `ClientPlayerInteractionManager.clickSlot`, with player-screen validation and slot mapping | `PlayerInventorySlotMapTest`; Fabric compilation |
| Equipment observation | `getEquipmentSlot` returned an item's default stack instead of its actual equipped stack | Return the equipped `ItemStack` | Fabric compilation |
| CCS switch back | Shared static `lastSlot` could be overwritten by another script; numeric switches did not save a return slot | Per-script `SelectionHistory`, world-identity check, manual override protection | `SelectionHistoryTest` |

### Design decisions

1. Keep CCS's public syntax intact where possible. No private PvP algorithms are introduced.
2. An action request is not a confirmed server result; synchronous inventory clicks still require observation by the calling script.
3. `InputLeaseManager` prevents stale scheduled releases, **but does not yet arbitrate physical input, external mods or all CCS owners**. Do not describe it as a complete `InputArbiter`.
4. `SelectionHistory` uses a weak reference to the world to avoid retaining a disconnected world. It handles a single pending return, not a multi-step transaction history.
5. 64-depth + command budget is a bounded improvement, not unlimited recursion.
6. Only the 1.21.11 branch is changed in this increment. No unverified gameplay behavior is claimed.

## Regression suite

The GitHub Actions workflow `.github/workflows/verify-pvp-runtime.yml` compiles pure Java test fixtures before the full Fabric build:

- `AsyncDecisionBridgeTest`
- `ScriptExecutionBudgetTest`
- `InputLeaseManagerTest`
- `SelectionHistoryTest`
- `PlayerInventorySlotMapTest`
- `TickTaskQueueTest`
- `KeyGestureTrackerTest`
- `ScriptExtensionsTest`
- `IncrementalPathfinderTest`
- `ScriptStateSmokeTest`
- `ScriptCommentsSmokeTest`
- `SenseCompareSmokeTest`
- `VersionStringsSmokeTest`

The CI run itself is authoritative for compilation and regression status. It is NOT a substitute for real Minecraft gameplay or PvP tests.

## Compatibility status

| Capability | 1.21.11 | 26.1.2 | 26.2 |
| --- | --- | --- | --- |
| Input lease hardening | Implemented in this branch; CI required | Not ported | Not ported |
| Script execution budget | Implemented in this branch; CI required | Not ported | Not ported |
| Async result cancellation | Implemented in this branch; CI required | Not ported | Not ported |
| Client inventory click normalization | Implemented in this branch; CI required | Requires adapter testing | Requires adapter testing |
| Per-script switch history | Implemented in this branch; CI required | Not ported | Not ported |
| Spear, mace and attribute swapping rules | Requires dedicated gameplay verification | Requires dedicated gameplay verification | Requires dedicated gameplay verification |
| Server-specific custom item matching | Not implemented | Not implemented | Not implemented |

## Outstanding architecture work (not claimed as completed)

- Full per-script and per-module `InputArbiter` across input, hotbar, offhand and movement.
- Immutable `CombatSnapshot` and typed `ActionResult`/unknown-state handling.
- Transactional `SequenceRuntime` with cancellation, timeouts and compensating actions.
- Item resolution by ID, Data Components, custom name, and ambiguity reporting.
- Safe cross-screen inventory operations and server-observed acknowledgement.
- Accurate critical eligibility and spear/mace attack modes.
- Version adapter interfaces for 1.21.11, 26.1.2 and 26.2.
- Integration tests for totem consumption, repeated swaps, Anchor Dual and world disconnects.
- Real in-game / server testing, FPS benchmarks, and video-specific mechanics research.

## Private script policy

Private personal `.ccs` implementations, loadouts and configs must remain outside public source, release assets and CI fixtures. This increment contains Java core changes, ordinary project documentation and synthetic Java regression tests only.

## Manual acceptance checklist (not yet executed)

1. Enable two independent synthetic script modules; alternate `switch <slot>` and `switch back` and verify isolation.
2. During a pending switch, change slot manually; verify the script does not override the new slot.
3. Open a non-player container; verify player-inventory-only operations fail closed rather than clicking an unrelated container slot.
4. Rapidly cancel and reacquire scripted button holds; verify old timeouts cannot release new holds.
5. Queue a compute result, cancel before application, and verify no side effects.
6. Disconnect while actions are pending and verify cleanup across the next world.
7. Repeat on a local test server under simulated latency before enabling new automation in public play.

This document reports the infrastructure increment only. It does not claim all enumerated PvP techniques were video-reviewed, implemented or gameplay-verified.
