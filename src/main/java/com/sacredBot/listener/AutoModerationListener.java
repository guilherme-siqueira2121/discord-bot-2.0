package com.sacredBot.listener;

import com.sacredBot.service.AutoModerationService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AutoModerationListener extends ListenerAdapter {

    private final AutoModerationService autoModerationService;

    public AutoModerationListener(AutoModerationService autoModerationService) {
        this.autoModerationService = autoModerationService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Member member = event.getMember();
        if (member == null) return;

        if (member.hasPermission(Permission.ADMINISTRATOR)) return;

        String content = event.getMessage().getContentRaw();

        if (autoModerationService.containsLink(content)) {
            punish(event, member, "envio de links");
            return;
        }

        if (autoModerationService.containsBlockedWord(content)) {
            punish(event, member, "uso de palavras proibidas");
        }
    }

    private void punish(MessageReceivedEvent event, Member member, String reason) {
        event.getMessage().delete().queue(
                success -> event.getChannel().sendMessage(
                        "🚫 " + member.getAsMention() + " sua mensagem foi removida por " + reason + "."
                ).queue()
        );
    }
}