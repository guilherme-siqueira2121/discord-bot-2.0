package com.sacredBot.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SlashCommandRegister implements ApplicationListener<ContextRefreshedEvent> {

    private final JDA jda;

    public SlashCommandRegister(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jda.upsertCommand(
                Commands.slash("nukar", "Deleta as últimas 100 mensagens do canal")
        ).queue();
    }
}