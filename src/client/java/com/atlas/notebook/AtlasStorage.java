package com.atlas.notebook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class AtlasStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type NOTE_LIST = new TypeToken<List<AtlasNote>>() {}.getType();

    private AtlasStorage() {}

    public static Path getFile(Minecraft client) {
        String worldKey = "no-world";
        if (client.getCurrentServer() != null) {
            worldKey = "server-" + client.getCurrentServer().ip;
        } else if (client.getSingleplayerServer() != null) {
            worldKey = "singleplayer-" + client.getSingleplayerServer().getWorldData().getLevelName();
        }
        worldKey = worldKey.replaceAll("[^a-zA-Z0-9._-]", "_");
        return client.gameDirectory.toPath().resolve("config").resolve("atlas").resolve(worldKey + ".json");
    }

    public static List<AtlasNote> load(Minecraft client) {
        Path file = getFile(client);
        if (!Files.exists(file)) return new ArrayList<>();
        try {
            List<AtlasNote> notes = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), NOTE_LIST);
            return notes == null ? new ArrayList<>() : new ArrayList<>(notes);
        } catch (Exception exception) {
            System.err.println("[Atlas] Could not read notes: " + exception.getMessage());
            return new ArrayList<>();
        }
    }

    public static void save(Minecraft client, List<AtlasNote> notes) {
        Path file = getFile(client);
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(notes, NOTE_LIST), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("[Atlas] Could not save notes: " + exception.getMessage());
        }
    }
}
