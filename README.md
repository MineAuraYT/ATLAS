# ATLAS — Adventure Notebook

**Author:** MineAura

A lightweight, client-side Fabric mod for Minecraft Java 26.2.

## Features in this first release

- Press **B** to open Atlas (change it in Options → Controls → Key Binds → Atlas).
- Create notes with a name, description, category/tag, and X/Y/Z coordinates.
- Automatically fill coordinates from your current position.
- Browse and search saved notes.
- Favorite important locations.
- Sort by name or distance from your current position.
- View, edit, delete, and copy coordinates.
- Notes are stored separately for each single-player save or multiplayer server.
- No teleportation and no gameplay/server changes.

## Storage

Notes are stored in `.minecraft/config/atlas/` as JSON files.

## Requirements

- Minecraft Java 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API 0.156.0+26.2
- Java/JDK 25
- Gradle 9.5.1 or newer (9.6.1 recommended)

## Build on Windows

1. Extract this source ZIP.
2. Open PowerShell in the `atlas-mod` folder.
3. Run:

   ```powershell
   .\BUILD-WINDOWS.ps1
   ```

4. The built mod JAR will be copied into `JAR`.

The script uses the Gradle installation on your PATH. It does not bundle a Gradle wrapper.

## Notes

This is an initial source release. The source and JSON structure have been checked, but an actual Minecraft/Gradle compile and in-game test must be completed on a machine with the required toolchain before treating the JAR as verified.
