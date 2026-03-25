package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class FileLoader {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public Optional<List<SleepingSession>> loadSessions(String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<SleepingSession> sessions = br.lines()
                    .map(line -> {
                        String[] parts = line.split(";");
                        LocalDateTime start = LocalDateTime.parse(parts[0], FORMATTER);
                        LocalDateTime end = LocalDateTime.parse(parts[1], FORMATTER);
                        SleepQuality quality = SleepQuality.valueOf(parts[2]);
                        return new SleepingSession(start, end, quality);
                    })
                    .toList();

            if (sessions.isEmpty()) {
                throw new IOException("Файл не содержит данных.");
            }
            return Optional.of(sessions);

        } catch (IOException e) {

            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return Optional.empty();
        }
    }
}
