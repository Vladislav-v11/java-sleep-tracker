package ru.yandex.practicum.sleeptracker.analisys;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class AverageDurationCalculator implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long average = (long) sessions.stream()
                .mapToLong(SleepingSession::getDuration)
                .average()
                .orElse(0);

        return new SleepAnalysisResult("Средняя продолжительность сна (мин)", average);
    }
}
