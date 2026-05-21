package com.sacredBot.listener.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class BanCommand implements Command {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("Você não tem permissão para usar este comando.").queue();
            return;
        }

        var mentions = event.getMessage().getMentions().getMembers();
        if (mentions.isEmpty()) {
            event.getChannel().sendMessage("Mencione um usuário. Ex: `eban @usuario motivo`").queue();
            return;
        }

        Member target = mentions.get(0);
        String reason = args.length > 2
                ? String.join(" ", Arrays.copyOfRange(args, 2, args.length))
                : "Sem motivo informado";

        if (!event.getMember().canInteract(target)) {
            event.getChannel().sendMessage("Você não pode banir este membro.").queue();
            return;
        }

        event.getChannel().sendMessage(
                "⚠️ Tem certeza que deseja banir **" + target.getUser().getName() + "**? Motivo: " + reason + "\nResponda com `sim` ou `não`."
        ).queue();

        event.getJDA().addEventListener(new ConfirmationListener(event.getAuthor().getId(), confirmed -> {
            if (confirmed) {
                target.ban(7, TimeUnit.DAYS).reason(reason).queue(
                        s -> event.getChannel().sendMessage("🔨 **" + target.getUser().getName() + "** foi banido. Motivo: " + reason).queue(),
                        e -> event.getChannel().sendMessage("Não foi possível banir este membro.").queue()
                );
            } else {
                event.getChannel().sendMessage("❌ Ação cancelada.").queue();
            }
        }));
    }
}