
# ClickCrystals (CC)

---------------------------------------------------------------

## Introducing ClickCrystalsScripts! (CCS)

Not enough modules? Your wonderful ideas aren't getting accepted?
Want private features but don't know how to code?
No worries, make your own modules with ClickCrystalsScripts (CCS)!

Introducing CCS, a fast and easy way to create modules from simple swapping,
to selective interactions in game!
CCS consists of a lot of CCS commands, and can be either
executed separately or together in a `.ccs` file, like a Minecraft
datapack!

Interested? Download our latest version and start creating
(CCS documentation coming soon on
[GitHub Wiki](https://github.com/itzispyder/clickcrystals/wiki))

Happy coding and cpvping!

\- ClickCrystal dev team

---------------------------------------------------------------

## Newest Changes
```yml
Add:
  - Entity hud
  - ClickCrystals XYZ website link
  - swap out ogredupe link with yessirbox
  - splash MOTD's to homescreen
  - second GUI mode

Patches:
  - huds being dumb when rendering over chat
  - config saving deleted modules

Remove:
  - remove packet mapper

CCS:
  - make [notify] command have an option for stay time
```

![demo](https://cdn.modrinth.com/data/YDYPZdGj/images/9b07a5d8c628b349386210cf537050bf73e21964.png)

## How Do I Use ClickCrystals?

### Keybindings

-----------------------------------------

| **Keybinding** |       **Description**       |
|----------------|:---------------------------:|
| APOSTROPHE     | Open module settings screen |
| COMMA          |   Custom commands prefix    |
| UP_ARROW       |     Message resend bind     |
| B              |        Zoom keybind         |
| COLON          |   Open hud editor screen    |

### Commands

-----------------------------------------

| **Command** | **Usage**                                             |              **Description**               |
|-------------|-------------------------------------------------------|:------------------------------------------:|
| .help       | .help `<module>`                                      |            Module info and help            |
| .debug      | .debug `<item>`                                       |       Sends ClickCrystals debug info       |
| .toggle     | .toggle `<module>` `[on,off,help]`                    |           Module toggle and help           |
| .gma        | .gma                                                  |  Command alias for `/gamemode adventure`   |
| .gmc        | .gmc                                                  |   Command alias for `/gamemode creative`   |
| .gms        | .gms                                                  |   Command alias for `/gamemode survival`   |
| .gmsp       | .gmsp                                                 |  Command alias for `/gamemode spectator`   |
| .keybinds   | .keybinds                                             |     Opens the keybinds setting screen      |
| .look       | .look `[at, rot, to]`                                 |            Look at a position.             |
| .pixelart   | .pixelart `<mode>` `values` `image url (https://...)` | Spawns in pixel art! (Requires GMC and OP) |
| .rotate     | .rotate `<pitch>` `<yaw>`                             |         Rotate to a pitch and yaw.         |

### Modules

-----------------------------------------

| **Module**       |                                   **Description**                                    |
|------------------|:------------------------------------------------------------------------------------:|
| ClickCrystal     |                          Binds crystal place to left click.                          |
| CrystalSwitch    |    Whenever you punch obsidian/bedrock with a sword, it will switch to a crystal.    |
| ObsidianSwitch   |                 Punch the ground with a sword to switch to obsidian.                 |
| PearlSwitchS     |                   Right click your sword to switch to pearl slot.                    |
| PearlSwitchT     |                     Right click a totem to switch to pearl slot.                     |
| AnchorSwitch     |   Whenever you place an anchor, switch to glowstone and back after you charge it.    |
| CrystalAnchor    |         Right click the ground with a crystal to switch to a respawn anchor.         |
| NoHurtCam        |                          Removes the annoying screen shake.                          |
| NoServerPacks    |          Prevents servers from forcing you to download their resource pack.          |
| SlowSwing        |                      Makes your hand swing like mining fatigue.                      |
| ToolSwitcher     |                       Switches to the right tool for the job.                        |
| FullBright       |                    Increases your gamma so you can actually see.                     |
| ModulesListHud   |                         Shows your active modules on screen.                         |
| NoGameOverlay    |                       Stops annoying overlays from rendering.                        |
| NoLevelLoading   |                    Prevents most loading screens from rendering.                     |
| AntiCrash        |                         Stop server to client crashes today!                         |
| SilkTouch        |                      Gives any tool you hold silk touch (Real)                       |
| TotemPops        |              Sends the totem pops of another player. (With pop counter)              |
| CrystalSpeed     |                             Displays your crystal speed.                             |
| ShieldSwitch     |                    Right click your sword to switch to a shield.                     |
| BrightOrange     |                 Renders a bright golden overlay similar to shaders.                  |
| TotemOverlay     |                   Displays a red overlay when not holding a totem.                   |
| MessageResend    |                Click UP_ARROW to resend your previously sent message.                |
| NoViewBob        |       Removes view bob (Original Minecraft setting but as a toggleable module)       |
| AutoRespawn      |             Clicks the respawn button when you die. (Immediate respawn)              |
| RenderOwnName    |                        Renders your name tag in third person.                        |
| ClientCrystals   |                 Kills the crystal client-side when you attack them.                  |
| NoItemBounce     |               Removes the item bounce animation for inventory updates.               |
| CCExtras         |              Enabling will allow servers to sudo you with "!cc -users"               |
| GuiBorders       |                      Render ClickCrystals GUI element's borders                      |
| AxeSwap          |                Swap to hotbar axe when attacking a shielding opponent                |
| SwordSwap        |                                 Opposite of AxeSwap                                  |
| GlowingEntities  | Entities glow in the dark (and not just a dark model, useless if FullBright enabled) |
| NoScoreboard     |                     Disables rendering of the scoreboard sidebar                     |
| ArmorHud         |                       Displays armor durability and item count                       |
| HealthAsBars     |                   Turns your vanilla health bar into a health-bar!                   |
| ExplodeParticles |                     Turns off annoying particles in crystal pvp                      |
| RailSwap         |                         Hotkeys to rails after shooting bow                          |
| TnTSwap          |                     Hotkeys to tnt minecart after placing rails                      |
| BowSwap          |                      Hotkeys to bow after placing tnt minecart                       |
| ViewModel        |                             Changes your hand view model                             |
| Zoom             |                                 Now you can zoom, yw                                 |
| ChatPrefix       |                 Chat prefixes and suffixes, and custom unicode fonts                 |
| GhostTotem       |                      Renders a totem in your hand when you die                       |
| InGameHuds       |                        Custom ClickCrystals info huds manager                        |
| GuiCursor        |                              Cursor center fix, etc...                               |
| GapSwap          |          Swaps to golden apple or enchanted golden apples by clicking sword          |
| EntityStatuses   |            Displays received entity status packets in chat for debugging             |
| AutoWalk         |                             Holds down the walk button.                              |
| MouseTaper       |          POV - you taped down your mouse button (for skyblock cobble gens)           |


## But isn't this cheating?
It is not a cheat. It does not automate any task, for every action the mod preforms, you have to click.
- This mod is not a macro, it does not click for you. It functions as a hotkey for slots.

| Server                  | Flags | Bannable |
|:------------------------|:------|:---------|
| `mcpvp`.club            | no    | yes      |
| west.`uspvp`.org        | no    | yes      |
| east.`uspvp`.org        | no    | yes      |
| `ogredupe`.minehut.gg   | no    | no       |
| `ipearlpvp`.minehut.gg  | no    | no       |
| play.`pvplegacy`.net    | no    | yes      |
| `donut`.net             | no    | yes      |
| `hypixel`.net           | no    | pending  |
| `cubecraft`.net         | no    | pending  |
| `firevanilla`.net       | no    | yes      |
| `shatteredvanilla`.net  | no    | no       |
| `pvphub`.me             | no    | yes      |
| `europemc`.org          | no    | pending  |
| play.`jackpot`.org      | no    | pending  |
| `kingsmp`.net           | no    | pending  |
| `l1festee1`.net         | no    | no       |
| `ironcadia`.minehut.gg  | no    | no       |
| `apollocpvp`.minehut.gg | no    | yes      |


## For this Mod You Will Need
```yml
Minecraft Java Edition: 1.20.1
Fabric Loader: 0.14.21 or higher
Fabric API: 1.20.1
To stop: complaining it is a cheat/macro.
```

## Have A Good One!
- Thanks for downloading ClickCrystals!
- Make sure to give it a star on my [GitHub](https://github.com/itzispyder/clickcrystals)
- Follow the project on [Modrinth](https://modrinth.com/mod/clickcrystals)
- Feeling generous today? [Help support our project!](https://paypal.com/paypalme/thetrouper)

## SPECIAL THANKS AND SHOUTOUT TO
```yml
OgreNetworks: For their wonderful mod showcase! https://www.youtube.com/watch?v=M95TDqW2p2k
TheTrouper: For documenting the mod, and giving me ideas!
breadandcarrots: For being a wonderful mod tester!
ClickCrystals (bro really changed his name to this): For creating ClickCrystals' TikTok account
Tesla Tower: Best NBT Creator frfr
I No One: Bro is fr my infinite ideas source + \#1 Bug Spotter
TaxEvasion: Thank you for your changes to AntiCrash and addition of no explosion particles!
```
