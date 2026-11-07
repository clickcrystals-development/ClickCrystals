# Minecraft 26.3 port — open blockers

**Status:** source migration required, not a playable or publishable 26.3 build.

This branch began from the upstream ClickCrystals 1.4.3 / Minecraft 26.2 source. Gradle, Loom, Fabric Loader, Fabric API and Mod Menu targets have been updated to the 26.3 toolchain. This does **not** imply binary compatibility.

## Confirmed CI result

The four-target GitHub Actions build attempts compile. On Minecraft 26.3 the Java compiler currently fails with numerous class/API incompatibilities, including:
- `org.lwjgl.glfw.GLFW` is not on the 26.3 compilation classpath;
- removed/relocated Blaze3D render pipeline classes including `GpuTexture` and `RenderPipeline`;
- changed Minecraft client classes, including `ItemInHandRenderer` and `AxeItem`.

The first compiler output is truncated after many errors. Do not fix these by simply adding an arbitrary dependency or forcing the older mappings.

## Migration strategy

1. Inventory all 26.3 compiler errors, group by subsystem (input, rendering, items, mixins, interactions).
2. Port the input and keybinding abstraction to supported 26.3 game interfaces, retaining user-customizable keys.
3. Refactor GUI and HUD rendering against 26.3 APIs. Validate all mixin targets and method signatures.
4. Correct item and block API changes and verify combat modules in a local test world.
5. Reintegrate the v1.4.4 scripting improvements from `mc/26.2`.
6. Require successful `./gradlew clean build`, game-start smoke tests, and module regression tests before release.

**Do not include this branch's JAR in a public v1.4.4 release until these gates pass.**

References:
- https://www.fabricmc.net/2026/09/15/263.html
- https://github.com/FabricMC/fabric-docs

## Русский

Эта ветка ещё не является готовым портом на Minecraft 26.3. Новая версия изменила классы ввода, рендеринга и Minecraft API. Простая смена `minecraft_version` не решает ошибки компиляции.

Перед выпуском нужно исправить несовместимые классы, проверить миксины, собрать JAR и протестировать мод в игре. До этого сборку 26.3 нельзя выдавать за работающую.
