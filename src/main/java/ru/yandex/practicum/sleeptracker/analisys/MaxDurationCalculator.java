package ru.yandex.practicum.sleeptracker.analisys;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class MaxDurationCalculator implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .mapToLong(SleepingSession::getDuration)
                .max()
                .orElse(0);

        return new SleepAnalysisResult("Максимальная продолжительность сна (мин)", max);
    }
}
