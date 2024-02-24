# 💎 ClickCrystals
<p align="center">
<img align="center" src="https://socialify.git.ci/ItziSpyder/ClickCrystals/image?description=1&descriptionEditable=Your%20ultimate%20crystal%20PvP%20assistance%2C%20ClickCrystals%20at%20your%20service.&font=Jost&forks=1&issues=1&language=1&logo=https%3A%2F%2Fclickcrystals.xyz%2Fcommon%2Fassets%2Ficon.png&name=1&owner=1&pattern=Solid&pulls=1&stargazers=1&theme=Auto" alt="ClickCrystals" width="640" height="320" />
</p>
<p align="center">
  <a href="#-key-features">Features</a> •
  <a href="#-introducing-clickcrystal-scripts">ClickScript</a> •
  <a href="#-newest-changes">Changelog</a> •
  <a href="#%EF%B8%8F-installation">Installation</a> •
  <a href="#-usage">Usage</a> •
  <a href="#-help">Help</a> •
  <a href="#-contributing">Contributing</a> •
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

## ❓ Help

- If you found a bug, please make a issue in the issue tracker.
- Suggestions are also welcome. Join our discord server to suggest!
- You can make texture packs for the ClickCrystals Client. Tell about them on our discord to get promoted.
- If you need help coding a ClickScript, feel free to ask us in our discord server.

<a href="https://discord.gg/GdNnK37Etw" target="_blank"><img src="https://dcbadge.vercel.app/api/server/GdNnK37Etw"></a>

## 🌈 Contributing

Your contributions are really welcome. Contribution not just includes PRs, but your suggestions are also counted as contributions. Make a PR to contribute!
