# 💎 ClickCrystals

<div align="center">
  <img src="https://clickcrystals.xyz/clickscript/img/icon.png" alt="ClickCrystals" width="200">
</div>
<p align="center">
  <strong>Your Crystal PvP Companion</strong>
  <br>
  ClickCrystals at your service
</p>

<p align="center">
  <a href="#-key-features">Features</a> •
  <a href="#-introducing-clickcrystal-scripts">ClickScript</a> •
  <a href="#-newest-changes">Changelog</a> •
  <a href="#%EF%B8%8F-installation">Installation</a> •
  <a href="#-usage">Usage</a> •
  <a href="#-help">Help</a>
  <a href="#-contributing">Contributing</a> •
</p>

<p align="center">
<a href="https://discord.gg/GdNnK37Etw">
  <img alt="Discord" src="https://img.shields.io/discord/1095079504516493404?label=Discord&logo=discord&style=flat-square">
</a>
  <a href="https://github.com/ItziSpyder/ClickCrystals/graphs/contributors">
    <img alt="Contributors" src="https://img.shields.io/github/contributors/ItziSpyder/ClickCrystals?style=flat-square">
  </a>
  <a href="https://github.com/ItziSpyder/ClickCrystals">
    <img alt="Code size" src="https://img.shields.io/github/languages/code-size/ItziSpyder/ClickCrystals?style=flat-square">
  </a>
  <a href="https://github.com/ItziSpyder/ClickCrystals">
    <img alt="Lines of Code" src="https://tokei.rs/b1/github/ItziSpyder/ClickCrystals?style=flat-square">
  </a>
</p>



## 🌟 Key Features

- **Modules**: Powerful modules, undetectable
- **HUD & GUI**: Huds, nice GUI
- **ClickScript**: Your own Crystalling Modules!
- **Supported**: 1.19.4-1.20.4

## ✨ Introducing ClickCrystal Scripts!

Not enough modules? Your wonderful ideas aren't getting accepted? Want private features but don't know how to code? No worries, make your own modules with ClickCrystalsScripts (CCS)!

Introducing CCS, a fast and easy way to create modules from simple swapping, to selective interactions in game! CCS consists of a lot of CCS commands, and can be either executed separately or together in a .ccs file, like a Minecraft datapack!

Interested? Download our latest version and start creating (CCS documentation available on <a href="https://clickcrystals.xyz/clickscript" target="_blank">wiki</a>)

## 📃 Newest Changes

```yaml
Updating to: 1.20.4

Script:
  - on respawn # im not stable
  - if chance_of (N) # im not stable
  - if input_active sprint # im not stable
  - input sprint # im not stable
  - damage nearest_entity (ID) # im not stable
  - damage any_entity # im not stable
  - def function (name) (CCS.. args)
  - def module (name)
  - def desc ("")
  - function (name)

Tweaks:
  - added option to disable bedrock interaction when using crystal and obsidian switch modules
  - script (ID) arguments are now supported with commas, essentially making an or operator

Patches:
  - make event bus use concurrentlinkedqueue
  - Target hud totem pop text
  - escape key toggling modules on pojav
  - Fixed pojav crashes for target hud

Modules:
  - no block/container interactions # i no one
  - no gui background # i no one
  - camera clip # i no one
  - block outline # i no one

Commands:
  - ,version
```

## ⚙️ Installation

1. **Install the Fabric Loader**:

   - Visit the Fabric Loader website at [https://fabricmc.net/use](https://fabricmc.net/use).
   - Select your Minecraft version from the dropdown menu. Supported 1.19.4-1.20.4.
   - Click the "Download Installer" button for the corresponding operating system (Windows, macOS, or Linux).
   - Run the installer and follow the on-screen instructions to install the Fabric Loader.

2. **Download the Mod**:

   - Find and download, the desired version of the ClickCrystals From Here: [Releases](https://github.com/ItziSpyder/ClickCrystals)
   - Ensure the mod version you download is compatible with your Minecraft and Fabric Loader versions.
   - Save the mod `.jar` file to your computer.

3. **Install ClickCrystals**:

   - Locate your Minecraft installation folder:
      - Windows: `%appdata%\.minecraft`
      - macOS: `~/Library/Application Support/minecraft`
      - Linux: `~/.minecraft`
   - Create a new folder named `mods` inside your Minecraft installation folder if it doesn't already exist.
   - Move the Mod `.jar` file you downloaded earlier into the `mods` folder.

4. **Launch Minecraft with Fabric**:

   - Open the Minecraft Launcher.
   - In the bottom-left corner, click the dropdown menu next to the "Play" button.
   - Select the "fabric-loader" profile.
   - Click "Play" to launch Minecraft with Fabric and your installed mods.

## 👉 Usage

After launching the game, go to a single-player world or multi-player. Then, use the apostrophe key to open the client menu.

<img src="https://clickcrystals.xyz/faq/assets/apostrophe.png" alt="Apostrophe Key">

## ⚙️ Modules

| **Module**         | **Description**                                                                |
|--------------------|--------------------------------------------------------------------------------|
| ClickCrystal       | Binds crystal place to left click.                                             |
| CrystalSwitch      | Whenever you punch obsidian/bedrock with a sword, it will switch to a crystal.|
| ObsidianSwitch     | Punch the ground with a sword to switch to obsidian.                           |
| PearlSwitchS       | Right click your sword to switch to pearl slot.                                |
| PearlSwitchT       | Right click a totem to switch to pearl slot.                                   |
| AnchorSwitch       | Whenever you place an anchor, switch to glowstone and back after you charge it.|
| CrystalAnchor      | Right click the ground with a crystal to switch to a respawn anchor.           |
| NoHurtCam          | Removes the annoying screen shake.                                             |
| NoServerPacks      | Prevents servers from forcing you to download their resource pack.              |
| SlowSwing          | Makes your hand swing like mining fatigue.                                      |
| ToolSwitcher       | Switches to the right tool for the job.                                         |
| FullBright         | Increases your gamma so you can actually see.                                   |
| ModulesListHud     | Shows your active modules on screen.                                            |
| NoGameOverlay      | Stops annoying overlays from rendering.                                         |
| NoLevelLoading     | Prevents most loading screens from rendering.                                   |
| AntiCrash          | Stop server to client crashes today!                                            |
| SilkTouch          | Gives any tool you hold silk touch (Real).                                      |
| TotemPops          | Sends the totem pops of another player. (With pop counter)                      |
| CrystalSpeed       | Displays your crystal speed.                                                    |
| ShieldSwitch       | Right click your sword to switch to a shield.                                   |
| BrightOrange       | Renders a bright golden overlay similar to shaders.                             |
| TotemOverlay       | Displays a red overlay when not holding a totem.                                |
| MessageResend      | Click UP_ARROW to resend your previously sent message.                          |
| NoViewBob          | Removes view bob (Original Minecraft setting but as a toggleable module)        |
| AutoRespawn        | Clicks the respawn button when you die. (Immediate respawn)                     |
| RenderOwnName      | Renders your name tag in third person.                                          |
| ClientCrystals     | Kills the crystal client-side when you attack them.                             |
| NoItemBounce       | Removes the item bounce animation for inventory updates.                        |
| CCExtras           | Enabling will allow servers to sudo you with "!cc -users"                       |
| GuiBorders         | Render ClickCrystals GUI element's borders                                      |
| AxeSwap            | Swap to hotbar axe when attacking a shielding opponent                           |
| SwordSwap          | Opposite of AxeSwap                                                             |
| GlowingEntities    | Entities glow in the dark (and not just a dark model, useless if FullBright enabled)|
| NoScoreboard       | Disables rendering of the scoreboard sidebar.                                   |
| ArmorHud           | Displays armor durability and item count.                                       |
| HealthAsBars       | Turns your vanilla health bar into a health-bar!                                |
| ExplodeParticles   | Turns off annoying particles in crystal pvp.                                    |
| RailSwap           | Hotkeys to rails after shooting bow.                                            |
| TnTSwap            | Hotkeys to tnt minecart after placing rails.                                   |
| BowSwap            | Hotkeys to bow after placing tnt minecart.                                      |
| ViewModel          | Changes your hand view model.                                                   |
| Zoom               | Now you can zoom, yw                                                             |
| ChatPrefix         | Chat prefixes and suffixes, and custom unicode fonts.                           |
| GhostTotem         | Renders a totem in your hand when you die.                                      |
| InGameHuds         | Custom ClickCrystals info huds manager.                                         |
| GuiCursor          | Cursor center fix, etc...                                                        |
| GapSwap            | Swaps to golden apple or enchanted golden apples by clicking sword.             |
| EntityStatuses     | Displays received entity status packets in chat for debugging.                  |
| AutoWalk           | Holds down the walk button.                                                      |
| MouseTaper         | POV - you taped down your mouse button (for skyblock cobble gens)               |


## ❓ Help

- If you found a bug, please make a issue in the issue tracker.
- Suggestions are also welcome. Join our discord server to suggest!
- You can make texture packs for the ClickCrystals Client. Tell about them on our discord to get promoted.
- If you need help coding a ClickScript, feel free to ask us in our discord server.

<a href="https://discord.gg/GdNnK37Etw" target="_blank"><img src="https://dcbadge.vercel.app/api/server/GdNnK37Etw"></a>

## 🌈 Contributing

Your contributions are really welcome. Contribution not just includes PRs, but your suggestions are also counted as contributions.

Thanks goes to these wonderful people:

<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://itzispyder.github.io/"><img src="https://avatars.githubusercontent.com/u/114215797?v=4?s=100" width="100px;" alt="ImproperIssues"/><br /><sub><b>ImproperIssues</b></sub></a><br /><a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/commits?author=ItziSpyder" title="Code">💻</a> <a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/commits?author=ItziSpyder" title="Documentation">📖</a> <a href="#ideas-ItziSpyder" title="Ideas, Planning, & Feedback">🤔</a> <a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/issues?q=author%3AItziSpyder" title="Bug reports">🐛</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://discord.gg/ogre"><img src="https://avatars.githubusercontent.com/u/93684527?v=4?s=100" width="100px;" alt="TheTrouper"/><br /><sub><b>TheTrouper</b></sub></a><br /><a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/commits?author=TheTrouper" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/ayaanibrahimtutla"><img src="https://avatars.githubusercontent.com/u/91965613?v=4?s=100" width="100px;" alt="Tutla"/><br /><sub><b>Tutla</b></sub></a><br /><a href="#ideas-ayaanibrahimtutla" title="Ideas, Planning, & Feedback">🤔</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://i-no-one.github.io/Website/"><img src="https://avatars.githubusercontent.com/u/145749961?v=4?s=100" width="100px;" alt="I-No-oNe"/><br /><sub><b>I-No-oNe</b></sub></a><br /><a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/commits?author=I-no-one" title="Code">💻</a> <a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/issues?q=author%3AI-no-one" title="Bug reports">🐛</a> <a href="#ideas-I-no-one" title="Ideas, Planning, & Feedback">🤔</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://ashishagarwal.is-a.dev"><img src="https://avatars.githubusercontent.com/u/83082760?v=4?s=100" width="100px;" alt="ashishagarwal2023"/><br /><sub><b>ashishagarwal2023</b></sub></a><br /><a href="https://github.com/ItziSpyder/ClickCrystalsXYZ/commits?author=ashishagarwal2023" title="Documentation">📖</a> <a href="#ideas-ashishagarwal2023" title="Ideas, Planning, & Feedback">🤔</a></td>
  </tbody>
</table>

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
