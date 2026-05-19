package com.sacredBot.listener.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TimeoutCommand implements Command {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (!event.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            event.getChannel().sendMessage("Você não tem permissão para usar este comando.").queue();
            return;
        }

        var mentions = event.getMessage().getMentions().getMembers();
        if (mentions.isEmpty() || args.length < 3) {
            event.getChannel().sendMessage("Uso correto: `etimeout @usuario <minutos> motivo`").queue();
            return;
        }

        Member target = mentions.get(0);

        long duration;
        try {
            duration = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Duração inválida. Informe em minutos. Ex: `etimeout @usuario 10 spam`").queue();
            return;
        }

        String reason = args.length > 3
                ? String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length))
                : "Sem motivo informado";

        if (!event.getMember().canInteract(target)) {
            event.getChannel().sendMessage("Você não pode aplicar timeout neste membro.").queue();
            return;
        }

        event.getChannel().sendMessage(
                "⚠️ Tem certeza que deseja silenciar **" + target.getUser().getName() + "** por " + duration + " minuto(s)? Motivo: " + reason + "\nResponda com `sim` ou `não`."
        ).queue();

        event.getJDA().addEventListener(new ConfirmationListener(event.getAuthor().getId(), confirmed -> {
            if (confirmed) {
                target.timeoutFor(duration, TimeUnit.MINUTES).reason(reason).queue(
                        s -> event.getChannel().sendMessage("🔇 **" + target.getUser().getName() + "** recebeu timeout de " + duration + " minuto(s). Motivo: " + reason).queue(),
                        e -> event.getChannel().sendMessage("Não foi possível aplicar timeout.").queue()
                );
            } else {
                event.getChannel().sendMessage("❌ Ação cancelada.").queue();
            }
        }));
    }
}