package com.atlas.notebook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class AtlasDetailsScreen extends Screen {
    private final Screen parent;
    private final AtlasNote note;

    public AtlasDetailsScreen(Screen parent, AtlasNote note) {
        super(Component.literal(note.name));
        this.parent = parent;
        this.note = note;
    }

    @Override
    protected void init() {
        int left = (this.width - 320) / 2;
        int top = (this.height - 170) / 2;
        this.addRenderableWidget(Button.builder(Component.literal(note.favorite ? "★ Unfavorite" : "☆ Favorite"), b -> {
            note.favorite = !note.favorite;
            save();
            this.minecraft.gui.setScreen(new AtlasDetailsScreen(parent, note));
        }).bounds(left, top + 112, 100, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Copy Coords"), b -> {
            this.minecraft.keyboardHandler.setClipboard(note.x + " " + note.y + " " + note.z);
        }).bounds(left + 108, top + 112, 100, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Edit"), b -> this.minecraft.gui.setScreen(new AtlasEditorScreen(parent, note)))
                .bounds(left + 216, top + 112, 48, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Delete"), b -> {
            List<AtlasNote> notes = AtlasStorage.load(Minecraft.getInstance());
            notes.removeIf(existing -> existing.id.equals(note.id));
            AtlasStorage.save(Minecraft.getInstance(), notes);
            this.minecraft.gui.setScreen(new AtlasScreen());
        }).bounds(left + 270, top + 112, 50, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Back"), b -> this.minecraft.gui.setScreen(new AtlasScreen()))
                .bounds(left + 110, top + 140, 100, 20).build());
    }

    private void save() {
        List<AtlasNote> notes = AtlasStorage.load(Minecraft.getInstance());
        notes.removeIf(existing -> existing.id.equals(note.id));
        notes.add(note);
        AtlasStorage.save(Minecraft.getInstance(), notes);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int left = (this.width - 344) / 2;
        int top = (this.height - 170) / 2;
        graphics.fill(left - 12, top - 12, left + 332, top + 174, 0xF0181820);
        graphics.fill(left - 12, top - 12, left + 332, top + 18, 0xFF56327A);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.text(this.font, note.name, left, top, 0xFFFFFFFF, true);
        graphics.text(this.font, "Tag: " + note.tag, left, top + 30, 0xFFDDDDDD, true);
        graphics.text(this.font, "Coordinates: X " + note.x + "  Y " + note.y + "  Z " + note.z, left, top + 48, 0xFFFFFFFF, true);
        graphics.text(this.font, "Dimension: " + note.dimension, left, top + 66, 0xFFBBBBBB, true);
        graphics.text(this.font, "Description:", left, top + 86, 0xFFDDDDDD, true);
        graphics.text(this.font, note.description.isBlank() ? "No description" : note.description, left + 82, top + 86, 0xFFFFFFFF, true);
    }
}
