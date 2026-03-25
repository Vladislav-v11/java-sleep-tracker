package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;

public class SleepingSession {

    private final LocalDateTime start;
    private final LocalDateTime finish;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime finish,  SleepQuality quality) {
        this.start = start;
        this.finish = finish;
        this.quality = quality;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDuration() {
        return Duration.between(start, finish).toMinutes();
    }
}
