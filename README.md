# ATLAS — Adventure Notebook

**Author:** MineAura  
**Minecraft:** Java Edition 26.2  
**Mod loader:** Fabric  
**Mod version:** 1.0.0  
**License:** MIT

ATLAS is a lightweight, client-side adventure notebook for Minecraft. Save useful locations in your world so you can find them again later.

## Features

- Open ATLAS with **B** by default.
- Change the keybind in **Options → Controls → Key Binds → Atlas**.
- Create location notes with a name, description, tag, and X/Y/Z coordinates.
- Automatically fill in your current coordinates when creating a note.
- Browse and search saved notes.
- Mark important locations as favorites.
- Sort notes by name or distance from your current position.
- View, edit, delete, and copy saved coordinates.
- Keep notes separate for each single-player world or multiplayer server.
- No teleportation feature.

## How to use

1. Install Fabric for Minecraft Java 26.2 and the required dependencies.
2. Launch the game and press **B** to open ATLAS.
3. Select **+ New Note**.
4. Enter a location name, description, tag, and coordinates.
5. Use **Use Current Coords** to fill in your current position, then save the note.
6. Search or browse your notes later. Select a note to view its details, favorite it, copy its coordinates, edit it, or delete it.

## Where notes are saved

ATLAS stores notes as JSON files in:

```text
.minecraft/config/atlas/
```

A separate file is used for each recognized single-player world or multiplayer server.

**Tip:** Back up this folder if you want to preserve your notes when moving to another computer or reinstalling Minecraft.

## Requirements

- Minecraft Java Edition **26.2**
- Fabric Loader **0.19.3 or newer**
- Fabric API **0.156.0+26.2**
- Java **25**

## Build from source on Windows

You will also need Gradle **9.5.1 or newer** available on your `PATH`.

1. Download and extract the ATLAS source ZIP.
2. Open PowerShell in the extracted `atlas-mod` project folder.
3. Run:

   ```powershell
   .\BUILD-WINDOWS.ps1
   ```

4. If the build succeeds, the script copies the compiled JAR into the `JAR` folder.

The build script uses the Gradle installation on your system; this project does not include a Gradle wrapper.

## Compatibility

This release targets **Minecraft 26.2 with Fabric**. Other Minecraft versions and mod loaders are not claimed to be supported by this release.

## License

ATLAS is distributed under the MIT License. See the [`LICENSE`](LICENSE) file for the full license text.

---

Made by **MineAura**.
