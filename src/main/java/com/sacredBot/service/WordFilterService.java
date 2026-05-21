package com.sacredBot.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WordFilterService {

    private static final Map<Character, Character> LEET_MAP = Map.ofEntries(
            Map.entry('0', 'o'),
            Map.entry('1', 'i'),
            Map.entry('3', 'e'),
            Map.entry('4', 'a'),
            Map.entry('5', 's'),
            Map.entry('7', 't'),
            Map.entry('@', 'a'),
            Map.entry('$', 's'),
            Map.entry('+', 't')
    );

    public boolean containsBlockedWord(String message) {
        String normalized = normalize(message);
        List<String> blockedWords = loadBlockedWords();

        return blockedWords.stream().anyMatch(normalized::contains);
    }

    private String normalize(String text) {
        String result = text.toLowerCase();

        StringBuilder sb = new StringBuilder();
        for (char c : result.toCharArray()) {
            sb.append(LEET_MAP.getOrDefault(c, c));
        }
        result = sb.toString();

        result = Normalizer.normalize(result, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        result = result.replaceAll("[^a-z0-9 ]", "");

        result = result.replaceAll("(?<=\\S) (?=\\S)", "");

        return result;
    }

    private List<String> loadBlockedWords() {
        try {
            var resource = new ClassPathResource("blocked-words.txt");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines()
                        .map(String::trim)
                        .map(this::normalize)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar blocked-words.txt: " + e.getMessage());
            return List.of();
        }
    }
}