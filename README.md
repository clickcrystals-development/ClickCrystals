
<div class="main-banner" style="border: 3px aqua solid; border-radius: 30px; border-style: outset; padding-left: 10px;">
  <h1>
    <img src="https://cdn.modrinth.com/data/YDYPZdGj/images/a073ea1b025661446c83ba40f1445c8c4d840ab9.png" style="width: 15%; transform: translateY(20px) rotateZ(-10deg);"/>
    ClickCrystals [1.20 - 1.20.1]
  </h1>
  <h5 style="margin-left: 15px">
    Because who needs right click anyway?
  </h5>
</div>

---------------------------------------------------------------
### A message from the developers to our fellow users:
We understand your concerns, and have been constantly making changes and thinking of new ways to 
improve upon this project. 

However, as much as to how you all consider ClickCrystals to be an unfair advantage, this mod's
sole purpose is to bring ease to hotkeying. There are no built-in macros, and there have 
been numerous instances of players being able to replicate what this mod does without 
using it, which relieves the use of the phrase "unfair advantage" on this mod.

Please do not go about assuming what this mod does in your head without getting to use it 
first, needless to say spreading misinformation about it.

With that said, all hotkey modules will be staying and not to be removed anytime in the 
future unless it is to be voted out by the ClickCrystals community.
Thank you for your cooperation.

All unfair advantages that have been previously removed:
- TrueSight
- NoBreakDelay
- PearlSwitch's swapping back feature (accused for auto double hand)
- CrystalSwitch's swapping back feature (accused for middle click swap)
- ObsidianSwitch's swapping back feature (accused for scaffold)
- ClickCrystals' Auto-clicker (accused for macroing and autoclicking)

Happy coding and cpvping!

\- ClickCrystal dev team

---------------------------------------------------------------

## Newest Changes
```yml
Minecraft: 1.20-1.20.1
Mod: 0.9.8

Changelog:
  Modules:
    - Added new category for Minecart PvP
    - Category names are now simplified
    - New module RailSwap
    - New module TnTSwap
    - New module BowSwap
    - New module Zoom
    - New module ViewModel
  Commands:
    - ",toggle" now opens module screen
    - ",keybind" for people that do not know how to set keybinds, it will open up the keybinds screen
  GUI:
    - Scroll panels now cannot scroll down infinitely
    - Scroll panels now have scroll bars
    - Module setting section that are empty no longer render
    - Category bar now displays in my own order instead of alphabetically
    - Search page now paginated
    - Search queries are now more accurate
  Patches:
    - Pixel art placing one block at a time, now it uses both /setblock and /fill
    - Chat box crashing when holding down back arrow key while typing a custom command
```

<div class="demo-menu">
  <img src="https://cdn.modrinth.com/data/YDYPZdGj/images/962b47bf0b3d8d50441a0c4b2498ea94c84fb365.png" style="border: 3px aqua solid; border-radius: 30px; border-style: outset;"/>
</div>

## How Do I Use ClickCrystals?

### Keybindings

-----------------------------------------

| **Keybinding** |       **Description**       |
|----------------|:---------------------------:|
| APOSTROPHE     | Open module settings screen |
| COMMA          |   Custom commands prefix    |
| UP_ARROW       |     Message resend bind     |
| B              |        Zoom keybind         |

### Commands

-----------------------------------------

| **Command** | **Usage**                                             |              **Description**               |
|-------------|-------------------------------------------------------|:------------------------------------------:|
| .help       | .help `<module>`                                      |            Module info and help            |
| .toggle     | .toggle `<module>` `[on,off,help]`                    |           Module toggle and help           |
| .gms        | .gms                                                  |   Command alias for `/gamemode survival`   |
| .gmc        | .gmc                                                  |   Command alias for `/gamemode creative`   |
| .gma        | .gma                                                  |  Command alias for `/gamemode adventure`   |
| .gmsp       | .gmsp                                                 |  Command alias for `/gamemode spectator`   |
| .debug      | .debug `<item>`                                       |       Sends ClickCrystals debug info       |
| .pixelart   | .pixelart `<mode>` `values` `image url (https://...)` | Spawns in pixel art! (Requires GMC and OP) |
| .keybinds   | .keybinds                                             |     Opens the keybinds setting screen      |

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
| IconHud          |                    Renders the ClickCrystals logo on the screen.                     |
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
