package com.wellartdev.mentionping;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

import java.time.Instant;
import java.util.Locale;

/**
 * Pings the player when their username is mentioned in chat.
 *
 * Chat scrolls fast during multiplayer — whispers, @-mentions and callouts
 * slip by unnoticed. MentionPing listens to incoming chat messages and the
 * moment the local player's username appears in one (case-insensitive), it
 * plays a soft note-block pling and flashes a yellow HUD notice above the
 * hotbar for 3 seconds so the mention can't be missed.
 *
 * Client-side only. No mixins, no networking, nothing persisted.
 */
public class MentionPing implements ClientModInitializer {
    public static final String MOD_ID = "mentionping";

    /** How long (ms) the mention notice stays on screen after a mention. */
    private static final long NOTICE_DURATION_MS = 3000;

    /** Timestamp of the last detected mention; read by the HUD element every frame. */
    private volatile long mentionAt = 0;

    @Override
    public void onInitializeClient() {
        // Incoming chat listener: fire the ping when the player's name shows up.
        ClientReceiveMessageEvents.CHAT.register(this::onChat);

        // HUD notice shown for a few seconds after each mention.
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "mention_ping"),
                (extractor, deltaTracker) -> {
                    if (System.currentTimeMillis() - mentionAt >= NOTICE_DURATION_MS) {
                        return;
                    }
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player == null) {
                        return;
                    }
                    String line = "You were mentioned in chat!";
                    int x = (mc.getWindow().getGuiScaledWidth() - mc.font.width(line)) / 2;
                    int y = mc.getWindow().getGuiScaledHeight() - 70;
                    extractor.text(mc.font, line, x, y, 0xFFFF55);
                });
    }

    /**
     * Fires whenever the client receives a chat message. Plays the ping
     * sound and arms the HUD notice when the player's username appears in
     * the message text (case-insensitive).
     */
    private void onChat(Component message, PlayerChatMessage signedMessage,
                        GameProfile sender, ChatType.Bound params, Instant timestamp) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        String name = mc.player.getGameProfile().getName();
        if (name == null || name.isEmpty()) {
            return;
        }
        String text = message.getString();
        if (text.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
            mentionAt = System.currentTimeMillis();
            // Bright, attention-grabbing pling — distinct from ArmorGuard's
            // low thud so alerts are instantly recognizable.
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 1.2f));
        }
    }
}
