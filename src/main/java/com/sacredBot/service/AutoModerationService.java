package com.sacredBot.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class AutoModerationService {

    private final WordFilterService wordFilterService;
    private final Pattern linkPattern = Pattern.compile(
            "https?://\\S+|www\\.\\S+|\\S+\\.com\\S*|discord\\.gg/\\S+"
    );
    private static final List<String> ALLOWED_DOMAINS = List.of(
            "tenor.com",
            "media.discordapp.net",
            "cdn.discordapp.com"
    );

    public AutoModerationService(WordFilterService wordFilterService) {
        this.wordFilterService = wordFilterService;
    }

    public boolean containsLink(String message) {
        if (!linkPattern.matcher(message).find()) return false;

        return linkPattern.matcher(message).results()
                .map(match -> match.group())
                .noneMatch(link -> ALLOWED_DOMAINS.stream().anyMatch(link::contains));
    }

    public boolean containsBlockedWord(String message) {
        return wordFilterService.containsBlockedWord(message);
    }
}