package ru.yandex.practicum.sleeptracker.analisys;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<String, Long> chronotypeCounts = sessions.stream()
                .collect(Collectors.groupingBy(
                        session -> {
                            LocalTime startTime = session.getStart().toLocalTime();
                            LocalTime endTime = session.getFinish().toLocalTime();

                            if (startTime.isAfter(LocalTime.of(23, 0))
                                    && endTime.isAfter(LocalTime.of(9, 0))) {
                                return "Сова";
                            } else if (startTime.isBefore(LocalTime.of(22, 0))
                                    && endTime.isBefore(LocalTime.of(7, 0))) {
                                return "Жаворонок";
                            } else {
                                return "Голубь";
                            }
                        },
                        Collectors.counting()
                ));

        List<Map.Entry<String, Long>> entries = chronotypeCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .toList();

        if (entries.size() > 1 && entries.get(0).getValue().equals(entries.get(1).getValue())) {
            return new SleepAnalysisResult("Ваш хронотип", "Голубь");
        } else {
            String mostRepeatedChronotype = entries.get(0).getKey();
            return new SleepAnalysisResult("Ваш хронотип", mostRepeatedChronotype);
        }

    }
}
