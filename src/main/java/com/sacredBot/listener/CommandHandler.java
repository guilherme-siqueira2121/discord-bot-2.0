package com.sacredBot.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {

    public void handle(MessageReceivedEvent event, String commandBody) {
        String commandName = commandBody.split(" ")[0].toLowerCase();

        switch (commandName) {
            case "ping" -> event.getChannel().sendMessage("Pong! 🏓").queue();
            case "oi"   -> event.getChannel()
                    .sendMessage("Olá, " + event.getAuthor().getName() + "!")
                    .queue();
            default     -> event.getChannel()
                    .sendMessage("Comando desconhecido. Tente `!ping` ou `!oi`.")
                    .queue();
        }
    }
}