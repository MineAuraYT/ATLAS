package com.atlas.notebook;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class AtlasClient implements ClientModInitializer {
    private static KeyMapping openAtlas;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("atlas", "controls"));
        openAtlas = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.atlas.open", InputConstants.Type.KEYSYM, InputConstants.KEY_B, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openAtlas.consumeClick()) {
                if (client.player != null && client.level != null && client.gui.screen() == null) {
                    client.gui.setScreen(new AtlasScreen());
                }
            }
        });
    }
}
