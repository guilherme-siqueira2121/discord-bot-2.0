package com.sacredBot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class KickListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("kick")) return;

        if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("Você não tem permissão para usar este comando.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        Member alvo   = event.getOption("membro").getAsMember();
        String motivo = event.getOption("motivo") != null
                ? event.getOption("motivo").getAsString()
                : "Sem motivo informado";

        if (alvo == null) {
            event.reply("Membro não encontrado neste servidor.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        if (!event.getMember().canInteract(alvo)) {
            event.reply("Você não pode expulsar este membro.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        alvo.kick()
                .reason(motivo)
                .queue(
                        success -> event.reply(
                                "👢 **" + alvo.getUser().getName() + "** foi expulso. Motivo: " + motivo
                        ).queue(),
                        error -> event.reply("Não foi possível expulsar este membro. Verifique se o bot tem cargo acima dele.")
                                .setEphemeral(true)
                                .queue()
                );
    }
}