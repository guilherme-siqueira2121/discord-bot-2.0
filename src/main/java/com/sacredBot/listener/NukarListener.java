package com.sacredBot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NukarListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("nukar")) return;

        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("Você não tem permissão para usar este comando.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.deferReply(true).queue();

        TextChannel channel = event.getChannel().asTextChannel();

        channel.getHistory().retrievePast(100).queue(messages -> {
            if (messages.isEmpty()) {
                event.getHook().sendMessage("Nenhuma mensagem encontrada.").queue();
                return;
            }

            Runnable onSuccess = () ->
                    event.getHook()
                            .sendMessage("🧨 " + messages.size() + " mensagens deletadas.")
                            .queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));

            if (messages.size() == 1) {
                messages.get(0).delete().queue(s -> onSuccess.run());
            } else {
                channel.deleteMessages(messages).queue(s -> onSuccess.run());
            }
        });
    }
}