package com.sacredBot.config;

import com.sacredBot.listener.MessageListener;
import com.sacredBot.listener.NukarListener;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventListenerConfig {

    public EventListenerConfig(JDA jda, MessageListener messageListener, NukarListener nukarListener) {
        jda.addEventListener(messageListener, nukarListener);
    }
}