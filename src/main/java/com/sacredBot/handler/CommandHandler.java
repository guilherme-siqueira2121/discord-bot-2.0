package com.sacredBot.handler;

import com.sacredBot.listener.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommandHandler {

    private final Map<String, Command> commands;

    public CommandHandler(@Qualifier("commands") Map<String, Command> commands) {
        this.commands = commands;
    }

    public void handle(MessageReceivedEvent event, String body) {
        String[] parts = body.split(" ");
        String name = parts[0].toLowerCase();

        Command command = commands.get(name);
        if (command == null) {
            return;
        }

        command.execute(event, parts);
    }
}