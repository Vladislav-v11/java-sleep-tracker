package ru.yandex.practicum.sleeptracker.analisys;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightCalculator implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        SleepingSession first = sessions.getFirst();
        SleepingSession last = sessions.getLast();

        LocalDate firstNight;
        if (first.getStart().getHour() < 12) {
            firstNight = first.getStart().toLocalDate();
        } else {
            firstNight = first.getStart().toLocalDate().plusDays(1);
        }

        LocalDate lastNight = last.getFinish().toLocalDate();

        long sleeplessNights = firstNight
                .datesUntil(lastNight.plusDays(1))
                .filter(night -> {
                    LocalDateTime start = night.atStartOfDay();
                    LocalDateTime end = night.atTime(6, 0);

                    return sessions.stream()
                            .noneMatch(s ->
                                    s.getStart().isBefore(end)
                                            && s.getFinish().isAfter(start)
                            );
                })
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }
}