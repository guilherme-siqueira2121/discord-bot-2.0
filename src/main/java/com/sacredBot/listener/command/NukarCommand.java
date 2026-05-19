package com.sacredBot.listener.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NukarCommand implements Command {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("Você não tem permissão para usar este comando.").queue();
            return;
        }

        event.getChannel().sendMessage(
                "⚠️ Tem certeza que deseja deletar as últimas 100 mensagens?\nResponda com `sim` ou `não`."
        ).queue();

        event.getJDA().addEventListener(new ConfirmationListener(event.getAuthor().getId(), confirmed -> {
            if (confirmed) {
                TextChannel channel = event.getChannel().asTextChannel();
                channel.getHistory().retrievePast(100).queue(messages -> {
                    if (messages.size() == 1) {
                        messages.get(0).delete().queue();
                    } else {
                        channel.deleteMessages(messages).queue();
                    }
                    channel.sendMessage("🧨 " + messages.size() + " mensagens deletadas.")
                            .queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                });
            } else {
                event.getChannel().sendMessage("❌ Ação cancelada.").queue();
            }
        }));
    }
}