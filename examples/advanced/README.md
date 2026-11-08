# Advanced CCS examples / Сложные CCS-модули

Requires **ClickCrystals Modified 1.4.5-dev.5**.

## AnchorDual v2 · B

- **Tap B**: attempts one placement → verified charge → detonation.
- **Hold B**: repeats one bounded sequence at a time, only after completion.
- State is maintained in `aa_stage`; `await_sense` handles client-observed placement, charging, and disappearance; `task after` leaves one tick for item selection before using anchors/glowstone.
- Supports working with an already-placed charged/uncharged anchor.
- Does not require a specific health value or a totem in any slot.
- Prevents a glowstone mainhand/offhand detonation attempt; restores the initial hotbar slot.

**This is NOT a blast-safe macro.** It does not verify target coordinates, collision geometry, or damage exposure. Moving your crosshair mid-sequence may cause an abort or incorrect completion. Only the Overworld/End should detonate anchors.

## SpearSwap v2 · V

- **Tap V**: one action. **Hold V**: repeat attempts when the initial weapon's attack cooldown is ready.
- Tests `attack_cooldown` **before** switching to a Lunge-enchanted spear.
- Immediately issues `switch #spear[lunge]` and `input attack` on the same client tick.
- Restores the original hotbar slot on the next game tick, guarding against overlapping attempts.
- Validates hunger ≥6 and that the player is neither in water nor gliding.
- No autoaim or arbitrary timer-based hold-attack loop.

**Java Edition warning:** Selecting a spear can force its own cooldown. A CCS command succeeding does **not** guarantee a Lunge event under client/server cooldown rules.

## Установка

1. Скачай файлы из этой папки и помести в `.clickcrystals/scripts/` игрового профиля.
2. Удали предыдущие `ger-anchor-dual` и `ger-lunge-v` скрипты, чтобы избежать дубликатов.
3. Выключи другие модули, использующие B и V.
4. Проверь оба модуля в одиночном мире. Для поиска ошибок нужны `.clickcrystals/current.log` и `logs/latest.log`.

## Verification

CI boots three Minecraft/Fabric versions with both scripts and verifies that they are loaded without CCS initialization errors. Only **actual gameplay tests** can establish whether timed item interactions or server-authoritative events complete successfully.

Author of Modified scripts: **Gergherobrine**.
