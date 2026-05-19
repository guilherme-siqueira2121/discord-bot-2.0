package com.sacredBot.config;

import com.sacredBot.listener.command.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CommandConfig {

    @Bean
    public Map<String, Command> commands(BanCommand ban, KickCommand kick,
                                         TimeoutCommand timeout, NukarCommand nukar) {
        return Map.of(
                "ban", ban,
                "kick", kick,
                "timeout", timeout,
                "nukar", nukar
        );
    }
}