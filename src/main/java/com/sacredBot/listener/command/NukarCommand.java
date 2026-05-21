package com.sacredBot.listener.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class NukarCommand implements Command {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("Você não tem permissão para usar este comando.").queue();
            return;
        }

        TextChannel original = event.getChannel().asTextChannel();

        event.getChannel().sendMessage(
                "⚠️ Tem certeza que deseja apagar todo o histórico deste canal?\nResponda com `sim` ou `não`."
        ).queue();

        event.getJDA().addEventListener(new ConfirmationListener(event.getAuthor().getId(), confirmed -> {
            if (!confirmed) {
                original.sendMessage("❌ Ação cancelada.").queue();
                return;
            }

            original.getGuild().createCopyOfChannel(original)
                    .setPosition(original.getPositionRaw()) // mantém a posição exata
                    .queue(clone -> {
                        TextChannel clonedChannel = (TextChannel) clone;

                        clonedChannel.getManager()
                                .setPosition(original.getPositionRaw())
                                .queue();

                        original.delete().queue(
                                success -> clonedChannel.sendMessage("🧨 Canal limpo com sucesso.").queue(),
                                error -> clonedChannel.sendMessage("❌ Erro ao deletar o canal original.").queue()
                        );
                    });
        }));
    }
}