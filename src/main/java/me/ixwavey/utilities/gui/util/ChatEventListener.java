package me.ixwavey.utilities.gui.util;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.ixwavey.utilities.gui.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ChatEventListener implements Listener {

    private final GUI<?> gui;

    public ChatEventListener(final GUI<?> gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        if (!e.getPlayer().getUniqueId().equals(gui.uuid)) return;
        e.setCancelled(true);
        gui.activeTextInput.onMessage(e);
        HandlerList.unregisterAll(this);
        gui.open(true);
    }
}