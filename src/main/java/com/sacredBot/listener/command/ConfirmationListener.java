package com.sacredBot.listener.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Consumer;

public class ConfirmationListener extends ListenerAdapter {

    private final String authorId;
    private final Consumer<Boolean> callback;

    public ConfirmationListener(String authorId, Consumer<Boolean> callback) {
        this.authorId = authorId;
        this.callback = callback;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(authorId)) return;

        String response = event.getMessage().getContentRaw().toLowerCase().trim();

        if (response.equals("sim")) {
            event.getJDA().removeEventListener(this);
            callback.accept(true);
        } else if (response.equals("não") || response.equals("nao")) {
            event.getJDA().removeEventListener(this);
            callback.accept(false);
        }
    }
}