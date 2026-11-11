package com.atlas.notebook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AtlasScreen extends Screen {
    private final List<AtlasNote> notes;
    private EditBox search;
    private int page;
    private boolean sortByDistance;
    private String lastSearch = "";

    public AtlasScreen() {
        super(Component.literal("Atlas - Adventure Notebook"));
        this.notes = AtlasStorage.load(Minecraft.getInstance());
    }

    @Override
    protected void init() {
        int left = Math.max(12, (this.width - 420) / 2);
        int top = Math.max(18, (this.height - 300) / 2);
        this.search = new EditBox(this.font, left + 12, top + 38, 270, 20, Component.literal("Search notes"));
        this.search.setHint(Component.literal("Search notes..."));
        this.search.setValue(lastSearch);
        this.addRenderableWidget(this.search);
        this.addRenderableWidget(Button.builder(Component.literal("+ New Note"), b -> openEditor(null))
                .bounds(left + 292, top + 38, 116, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(sortByDistance ? "Sort: Distance" : "Sort: Name"), b -> {
            sortByDistance = !sortByDistance;
            page = 0;
            rebuildWidgets();
        }).bounds(left + 12, top + 264, 120, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("‹"), b -> { page = Math.max(0, page - 1); rebuildWidgets(); })
                .bounds(left + 300, top + 264, 32, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("›"), b -> { page++; rebuildWidgets(); })
                .bounds(left + 338, top + 264, 32, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(left + 376, top + 264, 44, 20).build());
        addNoteButtons(left, top);
    }

    @Override
    public void tick() {
        super.tick();
        if (search != null && !search.getValue().equals(lastSearch)) {
            lastSearch = search.getValue();
            rebuildWidgets();
        }
    }

    private void addNoteButtons(int left, int top) {
        List<AtlasNote> visible = filteredNotes();
        int start = page * 7;
        int end = Math.min(visible.size(), start + 7);
        for (int i = start; i < end; i++) {
            AtlasNote note = visible.get(i);
            int row = i - start;
            String label = (note.favorite ? "★ " : "") + note.name + "  [" + note.x + ", " + note.y + ", " + note.z + "]";
            this.addRenderableWidget(Button.builder(Component.literal(label), b -> openDetails(note))
                    .bounds(left + 12, top + 68 + row * 26, 396, 22).build());
        }
    }

    private List<AtlasNote> filteredNotes() {
        String query = search == null ? "" : search.getValue().toLowerCase();
        List<AtlasNote> result = new ArrayList<>();
        for (AtlasNote note : notes) {
            if ((note.name + " " + note.description + " " + note.tag).toLowerCase().contains(query)) result.add(note);
        }
        if (sortByDistance && Minecraft.getInstance().player != null) {
            double px = Minecraft.getInstance().player.getX();
            double py = Minecraft.getInstance().player.getY();
            double pz = Minecraft.getInstance().player.getZ();
            result.sort(Comparator.comparingDouble(n -> Math.pow(n.x - px, 2) + Math.pow(n.y - py, 2) + Math.pow(n.z - pz, 2)));
        } else {
            result.sort(Comparator.comparing((AtlasNote n) -> !n.favorite).thenComparing(n -> n.name.toLowerCase()));
        }
        return result;
    }

    private void openEditor(AtlasNote note) {
        this.minecraft.gui.setScreen(new AtlasEditorScreen(this, note));
    }

    private void openDetails(AtlasNote note) {
        this.minecraft.gui.setScreen(new AtlasDetailsScreen(this, note));
    }

    public void refresh() {
        this.minecraft.gui.setScreen(new AtlasScreen());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int left = Math.max(12, (this.width - 420) / 2);
        int top = Math.max(18, (this.height - 320) / 2);

        // Draw the window first. Screen widgets are extracted afterwards so
        // their button backgrounds and labels stay visible above the panel.
        graphics.fill(left, top, left + 420, top + 320, 0xF0181820);
        graphics.fill(left, top, left + 420, top + 30, 0xFF56327A);
        graphics.fill(left + 1, top + 31, left + 419, top + 32, 0xFF8B62B8);

        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.text(this.font, "ATLAS  •  Adventure Notebook", left + 12, top + 10, 0xFFFFFFFF, true);
        graphics.text(this.font, "Saved locations: " + notes.size(), left + 12, top + 296, 0xFFE0E0E0, true);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(null);
    }
}
