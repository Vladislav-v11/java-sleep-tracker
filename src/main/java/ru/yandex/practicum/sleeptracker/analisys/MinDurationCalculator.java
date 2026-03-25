package ru.yandex.practicum.sleeptracker.analisys;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class MinDurationCalculator implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long min = sessions.stream()
                .mapToLong(SleepingSession::getDuration)
                .min()
                .orElse(0);
        return new SleepAnalysisResult("Минимальная продолжительность сна (мин)", min);
    }
}
