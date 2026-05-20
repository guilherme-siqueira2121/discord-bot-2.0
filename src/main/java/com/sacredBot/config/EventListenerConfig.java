package com.sacredBot.config;

import com.sacredBot.listener.AutoModerationListener;
import com.sacredBot.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventListenerConfig {

    public EventListenerConfig(JDA jda, MessageListener messageListener, AutoModerationListener autoModerationListener) {
        jda.addEventListener(messageListener, autoModerationListener);
    }
}