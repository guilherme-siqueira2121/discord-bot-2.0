package com.sacredBot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TimeoutListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("timeout")) return;

        if (!event.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("Você não tem permissão para usar este comando.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        Member alvo    = event.getOption("membro").getAsMember();
        long duracao   = event.getOption("duracao").getAsLong();
        String motivo  = event.getOption("motivo") != null
                ? event.getOption("motivo").getAsString()
                : "Sem motivo informado";

        if (alvo == null) {
            event.reply("Membro não encontrado neste servidor.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (!event.getMember().canInteract(alvo)) {
            event.reply("Você não pode aplicar timeout neste membro.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        alvo.timeoutFor(duracao, TimeUnit.MINUTES)
                .reason(motivo)
                .queue(
                        success -> event.reply(
                                "🔇 **" + alvo.getUser().getName() + "** recebeu timeout de "
                                        + duracao + " minuto(s). Motivo: " + motivo
                        ).queue(),
                        error -> event.reply("Não foi possível aplicar o timeout. Verifique se o bot tem cargo acima do membro.")
                                .setEphemeral(true)
                                .queue()
                );
    }
}