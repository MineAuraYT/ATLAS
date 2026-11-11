package com.atlas.notebook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class AtlasEditorScreen extends Screen {
    private final Screen parent;
    private final AtlasNote editing;
    private EditBox name;
    private EditBox description;
    private EditBox x;
    private EditBox y;
    private EditBox z;
    private EditBox tag;

    public AtlasEditorScreen(Screen parent, AtlasNote editing) {
        super(Component.literal(editing == null ? "New Atlas Note" : "Edit Atlas Note"));
        this.parent = parent;
        this.editing = editing;
    }

    @Override
    protected void init() {
        int left = (this.width - 320) / 2;
        int top = Math.max(12, (this.height - 250) / 2);
        name = field(left, top + 28, 320, 20, "Location name");
        description = field(left, top + 70, 320, 40, "Description");
        tag = field(left, top + 132, 320, 20, "Tag (Village, Stronghold, XP Farm...)");
        x = field(left, top + 174, 100, 20, "X");
        y = field(left + 110, top + 174, 100, 20, "Y");
        z = field(left + 220, top + 174, 100, 20, "Z");

        if (editing != null) {
            name.setValue(editing.name);
            description.setValue(editing.description);
            tag.setValue(editing.tag);
            x.setValue(Integer.toString(editing.x));
            y.setValue(Integer.toString(editing.y));
            z.setValue(Integer.toString(editing.z));
        } else if (Minecraft.getInstance().player != null) {
            x.setValue(Integer.toString(Minecraft.getInstance().player.getBlockX()));
            y.setValue(Integer.toString(Minecraft.getInstance().player.getBlockY()));
            z.setValue(Integer.toString(Minecraft.getInstance().player.getBlockZ()));
        }

        this.addRenderableWidget(Button.builder(Component.literal("Use Current Coords"), b -> fillCurrentCoords())
                .bounds(left, top + 204, 150, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> returnToParent())
                .bounds(left + 160, top + 204, 76, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Save Note"), b -> saveNote())
                .bounds(left + 244, top + 204, 76, 20).build());
    }

    private EditBox field(int x, int y, int width, int height, String hint) {
        EditBox box = new EditBox(this.font, x, y, width, height, Component.literal(hint));
        box.setHint(Component.literal(hint));
        this.addRenderableWidget(box);
        return box;
    }

    private void fillCurrentCoords() {
        if (this.minecraft.player == null) return;
        x.setValue(Integer.toString(this.minecraft.player.getBlockX()));
        y.setValue(Integer.toString(this.minecraft.player.getBlockY()));
        z.setValue(Integer.toString(this.minecraft.player.getBlockZ()));
    }

    private void saveNote() {
        if (name.getValue().isBlank()) {
            name.setHint(Component.literal("Please enter a name"));
            return;
        }
        try {
            AtlasNote note = editing == null ? new AtlasNote() : editing;
            note.name = name.getValue().trim();
            note.description = description.getValue().trim();
            note.tag = tag.getValue().isBlank() ? "General" : tag.getValue().trim();
            note.x = Integer.parseInt(x.getValue().trim());
            note.y = Integer.parseInt(y.getValue().trim());
            note.z = Integer.parseInt(z.getValue().trim());
            note.dimension = Minecraft.getInstance().level == null ? "Unknown" : Minecraft.getInstance().level.dimension().toString();
            List<AtlasNote> notes = AtlasStorage.load(Minecraft.getInstance());
            if (editing != null) notes.removeIf(existing -> existing.id.equals(editing.id));
            notes.add(note);
            AtlasStorage.save(Minecraft.getInstance(), notes);
            returnToParent();
        } catch (NumberFormatException exception) {
            System.err.println("[Atlas] Coordinates must be whole numbers.");
        }
    }

    private void returnToParent() {
        this.minecraft.gui.setScreen(parent instanceof AtlasScreen ? new AtlasScreen() : parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int left = (this.width - 344) / 2;
        int top = Math.max(12, (this.height - 250) / 2);
        graphics.fill(left - 12, top - 10, left + 332, top + 236, 0xF0181820);
        graphics.fill(left - 12, top - 10, left + 332, top + 18, 0xFF56327A);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.text(this.font, editing == null ? "Create a location note" : "Edit location note", left, top, 0xFFFFFFFF, true);
        graphics.text(this.font, "Name", left, top + 17, 0xFFDDDDDD, true);
        graphics.text(this.font, "Description", left, top + 59, 0xFFDDDDDD, true);
        graphics.text(this.font, "Category / tag", left, top + 121, 0xFFDDDDDD, true);
        graphics.text(this.font, "Coordinates (X / Y / Z)", left, top + 163, 0xFFDDDDDD, true);
    }
}
