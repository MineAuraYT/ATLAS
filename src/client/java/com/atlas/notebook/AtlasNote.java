package com.atlas.notebook;

import java.util.UUID;

public class AtlasNote {
    public String id = UUID.randomUUID().toString();
    public String name = "";
    public String description = "";
    public String tag = "General";
    public int x;
    public int y;
    public int z;
    public String dimension = "Unknown";
    public boolean favorite;
    public int color = 0xA65BEA;
    public long createdAt = System.currentTimeMillis();

    public AtlasNote() {}
}
