package com.sacredBot.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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
        jda.updateCommands().addCommands(

                Commands.slash("nukar", "Deleta as últimas 100 mensagens do canal"),

                Commands.slash("timeout", "Silencia um membro por um período determinado")
                        .addOptions(
                                new OptionData(OptionType.USER, "membro", "Membro a ser silenciado", true),
                                new OptionData(OptionType.INTEGER, "duracao", "Duração em minutos", true),
                                new OptionData(OptionType.STRING, "motivo", "Motivo do timeout", false)
                        ),

                Commands.slash("kick", "Exulsa um membro do servidor")
                        .addOptions(
                                new OptionData(OptionType.USER, "membro", "Membro a ser exulsa", true),
                                new OptionData(OptionType.STRING, "motivo", "Motivo do exulsa", false)
                        )

        ).queue();
    }
}