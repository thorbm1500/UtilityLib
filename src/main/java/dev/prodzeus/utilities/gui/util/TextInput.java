package dev.prodzeus.utilities.gui.util;

import io.papermc.paper.event.player.AsyncChatEvent;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@FunctionalInterface
public interface TextInput {

    static TextInput create(final Consumer<AsyncChatEvent> listener) {
        return listener::accept;
    }

    void onMessage(final AsyncChatEvent event);
}