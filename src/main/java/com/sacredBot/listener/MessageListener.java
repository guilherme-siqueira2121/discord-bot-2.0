package com.sacredBot.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends ListenerAdapter {

    @Value("${discord.prefix}")
    private String prefix;

    private final CommandHandler commandHandler;

    public MessageListener(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();

        if (content.startsWith(prefix)) {
            String commandBody = content.substring(prefix.length()).trim();
            commandHandler.handle(event, commandBody);
        }
    }
}